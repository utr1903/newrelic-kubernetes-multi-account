package main

import (
	"bravo-persistence-service/commons"
	"bravo-persistence-service/controllers"
	"bravo-persistence-service/data"

	"github.com/gin-gonic/gin"
)

const PORT string = ":8080"

func main() {
	nrapp := commons.CreateNewRelicAgent()
	dbClient := data.CreateDbClient()

	router := gin.Default()
	controllers.CreateHandlers(router, nrapp, dbClient)
	router.Run(PORT)
}
