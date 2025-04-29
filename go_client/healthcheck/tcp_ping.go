package healthcheck

import (
	"github.com/matsuridayo/libneko/speedtest"
)

const (
	pingTimeoutMilliseconds = 1000
)

func TcpPing(address string) (int32, error) {
	return speedtest.TcpPing(address, pingTimeoutMilliseconds)
}
