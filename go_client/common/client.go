package common

import "sync"

type vpnClient interface {
	Connect() error
	Disconnect() error
	Refresh() error
}

type CommonClient struct {
	mu         sync.Mutex
	vpnClients map[string]vpnClient
}

func (c *CommonClient) Connect(clientName string) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	if client, ok := c.vpnClients[clientName]; ok {
		return client.Connect()
	}
	return nil
}

func (c *CommonClient) Disconnect(clientName string) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	if client, ok := c.vpnClients[clientName]; ok {
		return client.Disconnect()
	}
	return nil
}

func (c *CommonClient) Refresh(clientName string) error {
	c.mu.Lock()
	defer c.mu.Unlock()
	if client, ok := c.vpnClients[clientName]; ok {
		return client.Refresh()
	}
	return nil
}

func (c *CommonClient) SetVpnClient(clientName string, client vpnClient) {
	c.mu.Lock()
	defer c.mu.Unlock()
	if c.vpnClients == nil {
		c.vpnClients = make(map[string]vpnClient)
	}
	c.vpnClients[clientName] = client
}

func (c *CommonClient) GetClientNames() []string {
	c.mu.Lock()
	defer c.mu.Unlock()
	var names []string
	for name := range c.vpnClients {
		names = append(names, name)
	}
	return names
}

func (c *CommonClient) RefreshAll() error {
	c.mu.Lock()
	defer c.mu.Unlock()
	for _, client := range c.vpnClients {
		if err := client.Refresh(); err != nil {
			return err
		}
	}
	return nil
}

var Client = &CommonClient{
	vpnClients: make(map[string]vpnClient),
}
