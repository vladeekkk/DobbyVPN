package healthcheck

import (
	"go_client/common"
	"net"
)

type Server struct {
	logger  Logger
	handler *handler
	dialer  *net.Dialer
	config  HealthSettings
	vpn     vpnHealth
}

func NewServer(config HealthSettings,
	logger Logger,
) *Server {
	return &Server{
		logger:  logger,
		handler: newHandler(),
		dialer: &net.Dialer{
			Resolver: &net.Resolver{
				PreferGo: true,
			},
		},
		config: config,
		vpn: vpnHealth{
			client:      common.Client,
			healthyWait: *config.VPN.Initial,
		},
	}
}
