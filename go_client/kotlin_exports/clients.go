package kotlin_exports

import (
	"go_client/cloak"
	"go_client/outline"
)

type OutlineClient *outline.OutlineClient

func NewOutlineClient(transportConfig string) OutlineClient {
	return outline.NewClient(transportConfig)
}

func StartCloakClient(localHost, localPort, config string, udp bool) {
	cloak.StartCloakClient(localHost, localPort, config, udp)
}

func StopCloakClient() {
	cloak.StopCloakClient()
}
