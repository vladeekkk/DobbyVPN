package healthcheck

import (
	"context"
	"go_client/common"
	"time"
)

type vpnHealth struct {
	client       *common.CommonClient
	healthyWait  time.Duration
	healthyTimer *time.Timer
}

func (s *Server) onUnhealthyVPN(ctx context.Context, lastErrMessage string) {
	s.logger.Info("program has been unhealthy for " +
		s.vpn.healthyWait.String() + ": restarting VPN (healthcheck error: " + lastErrMessage + ")")
	s.logger.Info("👉 See https://github.com/qdm12/gluetun-wiki/blob/main/faq/healthcheck.md")
	s.logger.Info("DO NOT OPEN AN ISSUE UNLESS YOU READ AND TRIED EACH POSSIBLE SOLUTION")
	names := s.vpn.client.GetClientNames()
	for _, name := range names {
		s.vpn.client.Refresh(name) // TODO: handle error
	}
	s.vpn.healthyWait += *s.config.VPN.Addition
	s.vpn.healthyTimer = time.NewTimer(s.vpn.healthyWait)
}
