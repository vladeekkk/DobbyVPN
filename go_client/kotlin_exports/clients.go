package kotlin_exports

import (
	"go_client/cloak"
	"go_client/outline"
)

type OutlineClient struct {
	*outline.OutlineClient
}

func (c *OutlineClient) Connect() error {
	return c.OutlineClient.Connect()
}

func (c *OutlineClient) Disconnect() error {
	return c.OutlineClient.Disconnect()
}

//func (c *OutlineClient) GetServerIP() net.IP {
//	return c.OutlineClient.GetServerIP()
//}

func (c *OutlineClient) Read() ([]byte, error) {
	return c.OutlineClient.Read()
}

func (c *OutlineClient) Write(buf []byte) (int, error) {
	return c.OutlineClient.Write(buf)
}

func NewOutlineClient(transportConfig string) *OutlineClient {
	cl := outline.NewClient(transportConfig)
	return &OutlineClient{OutlineClient: cl}
}

func StartCloakClient(localHost, localPort, config string, udp bool) {
	cloak.StartCloakClient(localHost, localPort, config, udp)
}

func StopCloakClient() {
	cloak.StopCloakClient()
}
