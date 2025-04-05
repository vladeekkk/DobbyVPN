package outline

import (
	"go_client/common"
	"go_client/outline/internal"
	"net"
)

const Name = "outline"

type OutlineClient struct {
	device *internal.OutlineDevice
	config string
}

func NewClient(transportConfig string) (*OutlineClient, error) {
	od, err := internal.NewOutlineDevice(transportConfig)
	if err != nil {
		return nil, err
	}
	return &OutlineClient{device: od, config: transportConfig}, nil
}

func (c *OutlineClient) Connect() error {
	od, err := internal.NewOutlineDevice(c.config)
	if err != nil {
		return err
	}

	c.device = od
	return nil
}

func (c *OutlineClient) Disconnect() error {
	return c.device.Close()
}

func (c *OutlineClient) Refresh() error {
	return c.device.Refresh()
}

func (c *OutlineClient) GetServerIP() net.IP {
	return c.device.GetServerIP()
}

func StartOutline(config string) error {
	client, err := NewClient(config)
	if err != nil {
		return err
	}

	common.Client.SetVpnClient(Name, client)
	return nil
}

func StopOutline() error {
	return common.Client.Disconnect(Name)
}
