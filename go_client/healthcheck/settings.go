package healthcheck

import "time"

// HealthSettings contains settings for the healthcheck and health server.
type HealthSettings struct {
	// ServerAddress is the listening address
	// for the health check server.
	// It cannot be the empty string in the internal state.
	ServerAddress string
	// ReadHeaderTimeout is the HTTP server header read timeout
	// duration of the HTTP server. It defaults to 100 milliseconds.
	ReadHeaderTimeout time.Duration
	// ReadTimeout is the HTTP read timeout duration of the
	// HTTP server. It defaults to 500 milliseconds.
	ReadTimeout time.Duration
	// TargetAddress is the address (host or host:port)
	// to TCP dial to periodically for the health check.
	// It cannot be the empty string in the internal state.
	TargetAddress string
	// SuccessWait is the duration to wait to re-run the
	// healthcheck after a successful healthcheck.
	// It defaults to 5 seconds and cannot be zero in
	// the internal state.
	SuccessWait time.Duration
	// VPN has health settings specific to the VPN loop.
	VPN HealthyWait
}

type HealthyWait struct {
	// Initial is the initial duration to wait for the program
	// to be healthy before taking action.
	// It cannot be nil in the internal state.
	Initial *time.Duration
	// Addition is the duration to add to the Initial duration
	// after Initial has expired to wait longer for the program
	// to be healthy.
	// It cannot be nil in the internal state.
	Addition *time.Duration
}
