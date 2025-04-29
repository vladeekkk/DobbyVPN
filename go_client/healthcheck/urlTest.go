package healthcheck

import (
	"github.com/matsuridayo/libneko/speedtest"
	"net/http"
)

const (
	urlTestTimeoutMilliseconds = 1000
)

var httpClient = &http.Client{}

func UrlTest(url string, standard int) (int32, error) {
	return speedtest.UrlTest(httpClient, url, urlTestTimeoutMilliseconds, standard)
}
