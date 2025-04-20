package healthcheck

import (
	"context"
	"go_client/common"
	"sync/atomic"
	"time"
)

type healthChecker struct {
	ctx         context.Context
	cancel      context.CancelFunc
	sendMetrics bool
	period      int32
}

var lastStatus atomic.Pointer[healthCheckStatus]

func newHealthCheck(sendMetrics bool, period int32) *healthChecker {
	ctx, cancel := context.WithCancel(context.Background())
	return &healthChecker{ctx: ctx, cancel: cancel, sendMetrics: sendMetrics, period: period}
}

func (h *healthChecker) stop() {
	h.cancel()
}

func (h *healthChecker) start() {
	ticker := time.NewTicker(time.Duration(h.period) * time.Second)
	for {
		select {
		case <-ticker.C:
			status := checkHealth()
			if !status.isHealthy {
				err := common.Client.RefreshAll()
				status.reconnectError = err
				status.reconnected = err == nil
			}
			lastStatus.Store(&status)
			if h.sendMetrics {
				h.sendStatusToServer(status)
			}

		case <-h.ctx.Done():
			ticker.Stop()
			return
		}
	}
}

func (h *healthChecker) sendStatusToServer(status healthCheckStatus) {
	// TODO: implement
}

func checkHealth() healthCheckStatus {
	ms, err := UrlTest("https://google.com", 1)
	return healthCheckStatus{at: time.Now(), isHealthy: err == nil, handshakeMs: ms, err: err}
}

type healthCheckStatus struct {
	at          time.Time
	isHealthy   bool
	handshakeMs int32
	err         error

	reconnected    bool
	reconnectError error
}
