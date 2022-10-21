package controllers

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/newrelic/go-agent/v3/integrations/nrgin"
	"github.com/newrelic/go-agent/v3/newrelic"
	"github.com/prometheus/client_golang/prometheus/promhttp"

	"bravo-persistence-service/data"
	"bravo-persistence-service/services/create"
	"bravo-persistence-service/services/delete"
	"bravo-persistence-service/services/list"
)

func CreateHandlers(
	router *gin.Engine,
	nrapp *newrelic.Application,
	dbClient *data.DbClient,
) {

	router.Use(nrgin.Middleware(nrapp))

	createHandler := create.CreateHandler{
		DbClient: dbClient,
	}

	listHandler := list.ListHandler{
		DbClient: dbClient,
	}

	deleteHandler := delete.DeleteHandler{
		DbClient: dbClient,
	}

	persistence := router.Group("/persistence")
	{
		// Health check
		persistence.GET("/health", func(ginctx *gin.Context) {
			ginctx.JSON(http.StatusOK, gin.H{
				"message": "OK!",
			})
		})

		// Prometheus
		persistence.GET("/metrics", prometheusHandler())

		// Create method
		persistence.POST("/create", createHandler.Run)

		// List method
		persistence.GET("/list", listHandler.Run)

		// Delete method
		persistence.DELETE("/delete", deleteHandler.Run)
	}
}

func prometheusHandler() gin.HandlerFunc {
	h := promhttp.Handler()

	return func(c *gin.Context) {
		h.ServeHTTP(c.Writer, c.Request)
	}
}
