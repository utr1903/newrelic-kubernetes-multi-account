package data

import (
	"bravo-persistence-service/data/mongo"
	"bravo-persistence-service/data/redis"
	"bravo-persistence-service/entities"

	"github.com/gin-gonic/gin"
)

type DbClient struct {
	MongoDbClient *mongo.MongoDbClient
	RedisClient   *redis.RedisClient
}

func CreateDbClient() *DbClient {

	var dbClient *DbClient = &DbClient{
		MongoDbClient: mongo.CreateMongoDbInstance(),
		RedisClient:   nil, //redis.CreateRedisInstance(),
	}
	return dbClient
}

func (dbClient DbClient) Insert(
	ginctx *gin.Context,
	entity *entities.Entity,
) error {

	// Save entity to DB
	err := dbClient.MongoDbClient.Insert(ginctx, entity)
	if err != nil {
		return err
	}

	return nil
	// err = dbClient.RedisClient.Insert(entity)
	// return err
}

func (dbClient DbClient) FindAll(
	ginctx *gin.Context,
	limit *int64,
) (
	*[]entities.Entity,
	error,
) {

	// Retrieve all entities from DB
	values, err := dbClient.MongoDbClient.FindAll(ginctx, limit)
	if err != nil {
		return nil, err
	}

	return values, nil
}

func (dbClient DbClient) Delete(
	ginctx *gin.Context,
	entityId string,
) error {

	// Retrieve all entities from DB
	err := dbClient.MongoDbClient.Delete(ginctx, entityId)
	if err != nil {
		return err
	}

	return nil
}
