package singbox

import (
	"context"
	"errors"
	"fmt"
	box "github.com/sagernet/sing-box"
	"github.com/sagernet/sing-box/option"
	"go_client/common"
	"sync"
)

const Name = "singBox"

type Client struct {
	box     *box.Box
	cancel  context.CancelFunc
	options option.Options
	mu      sync.Mutex
}

var mainInstance *Client
var mu sync.Mutex

func newClient(config string) (c *Client, err error) {
	ctx, cancel := context.WithCancel(context.Background())
	defer func() {
		if err != nil {
			cancel()
		}
	}()

	var options option.Options
	err = options.UnmarshalJSONContext(ctx, []byte(config))
	if err != nil {
		return nil, fmt.Errorf("decode config: %v", err)
	}
	sb, err := newBoxInstance(ctx, options)
	if err != nil {
		return nil, err
	}

	return &Client{box: sb, cancel: cancel, options: options}, nil
}

func (c *Client) Connect() error {
	mu.Lock()
	defer mu.Unlock()
	if c == nil {
		return errors.New("singbox client is missing")
	}
	if c.box == nil {
		ctx, cancel := context.WithCancel(context.Background())
		sb, err := newBoxInstance(ctx, c.options)
		if err != nil {
			cancel()
			return err
		}
		c.cancel = cancel
		c.box = sb
	}
	if err := c.box.Start(); err != nil {
		c.cancel()
		return err
	}
	return nil
}

func (c *Client) Disconnect() error {
	mu.Lock()
	defer mu.Unlock()
	if c == nil || c.box == nil {
		return nil
	}
	err := c.box.Close()
	c.cancel()
	c.box = nil
	return err
}

func (c *Client) Refresh() error {
	if err := c.Disconnect(); err != nil {
		return err
	}

	return c.Connect()
}

func newBoxInstance(ctx context.Context, options option.Options) (*box.Box, error) {
	return box.New(box.Options{
		Options:           options,
		Context:           ctx,
		PlatformLogWriter: nil, // TODO: add logger
	})
}

func StartSingBox(config string) error {
	mu.Lock()
	defer mu.Unlock()
	client, err := newClient(config)
	if err != nil {
		return err
	}
	mainInstance = client
	if err := mainInstance.Connect(); err != nil { // TODO: handle error with more details
		return err
	}
	common.Client.SetVpnClient(Name, client)
	return nil
}

func StopSingBox() error {
	mu.Lock()
	defer mu.Unlock()
	if mainInstance == nil {
		return nil
	}
	return mainInstance.Disconnect()
}
