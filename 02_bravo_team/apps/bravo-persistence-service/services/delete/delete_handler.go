package delete

import (
	"errors"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/rs/zerolog"

	"bravo-persistence-service/commons"
	"bravo-persistence-service/data"
	dto "bravo-persistence-service/dtos"
)

type DeleteHandler struct {
	DbClient *data.DbClient
}

func (handler DeleteHandler) Run(
	ginctx *gin.Context,
) {

	// Log start of method execution
	commons.LogWithContext(ginctx, zerolog.InfoLevel, "Delete method is triggered...")

	// Parse request param
	entityId, err := handler.parseRequestParam(ginctx)
	if err != nil {
		return
	}

	// Delete entity from DB
	err = handler.DbClient.Delete(ginctx, entityId)
	if err != nil {
		commons.CreateFailedHttpResponse(ginctx, http.StatusInternalServerError,
			"Entity could not be deleted from the DB.")
		return
	}

	commons.CreateSuccessfulHttpResponse(ginctx, http.StatusOK,
		handler.createResponseDto(entityId))

	// Log end of method execution
	commons.LogWithContext(ginctx, zerolog.InfoLevel, "Delete method is executed.")
}

func (DeleteHandler) parseRequestParam(
	ginctx *gin.Context,
) (
	string,
	error,
) {

	// Parse request param
	entityId := ginctx.Query("id")
	if entityId == "" {
		commons.CreateFailedHttpResponse(ginctx, http.StatusBadRequest,
			"Entity ID [id] field is empty.")

		return "", errors.New("entity id field is empty")
	}

	// Log provided id
	commons.LogWithContext(ginctx, zerolog.InfoLevel, "Id provided: "+entityId)

	return entityId, nil
}

func (DeleteHandler) createResponseDto(
	entityId string,
) *dto.ResponseDto {
	return &dto.ResponseDto{
		Message: "Entity is successfully deleted.",
		Data:    entityId,
	}
}
