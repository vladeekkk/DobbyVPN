package outline

import (
	"go_client/common"
	"go_client/outline/internal"
	"net"

	_ "go_client/logger"
)

const Name = "outline"

type OutlineClient struct {
	device *internal.OutlineDevice
	config string
}

func NewClient(transportConfig string) *OutlineClient {
	c := &OutlineClient{config: transportConfig}
	common.Client.SetVpnClient(Name, c)
	return c
}

func (c *OutlineClient) Connect() error {
	od, err := internal.NewOutlineDevice(c.config)
	if err != nil {
		return err
	}

	c.device = od
	common.Client.MarkActive(Name)
	return nil
}

func (c *OutlineClient) Disconnect() error {
	err := c.device.Close()
	if err != nil {
		return err
	}
	common.Client.MarkInactive(Name)
	return nil
}

func (c *OutlineClient) Refresh() error {
	return c.device.Refresh()
}

func (c *OutlineClient) GetServerIP() net.IP {
	return c.device.GetServerIP()
}

func (c *OutlineClient) Read() ([]byte, error) {
	return c.device.Read()
}

func (c *OutlineClient) Write(buf []byte) (int, error) {
	return c.device.Write(buf)
}
