package list

import (
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/rs/zerolog"

	"bravo-persistence-service/commons"
	"bravo-persistence-service/data"
	dto "bravo-persistence-service/dtos"

	"bravo-persistence-service/entities"
)

type ListHandler struct {
	DbClient *data.DbClient
}

func (handler ListHandler) Run(
	ginctx *gin.Context,
) {

	// Log start of method execution
	commons.LogWithContext(ginctx, zerolog.InfoLevel, "Create method is triggered...")

	// Parse request param
	limit, err := handler.parseRequestParam(ginctx)
	if err != nil {
		return
	}
	// Retrieve all values DB
	values, err := handler.DbClient.FindAll(ginctx, limit)
	if err != nil {
		commons.CreateFailedHttpResponse(ginctx, http.StatusInternalServerError,
			"Entity could not be saved into the DB.")
		return
	}

	commons.CreateSuccessfulHttpResponse(ginctx, http.StatusOK,
		handler.createResponseDto(values))

	// Log end of method execution
	commons.LogWithContext(ginctx, zerolog.InfoLevel, "List method is executed.")
}

func (ListHandler) parseRequestParam(
	ginctx *gin.Context,
) (
	*int64,
	error,
) {

	// Parse request param
	limitAsString := ginctx.Query("limit")

	var limit *int64
	if limitAsString == "" {
		// Nothing specific declared -> Set to nil
		limit = nil

		// Log provided limit
		commons.LogWithContext(ginctx, zerolog.InfoLevel, "No limit provided")
	} else {
		// Specific limit declared -> Parse
		limitAsInt, err := strconv.ParseInt(limitAsString, 10, 64)
		if err != nil {
			commons.CreateFailedHttpResponse(ginctx, http.StatusInternalServerError,
				"Given limit parameter is not valid.")
			return nil, err
		}
		limit = &limitAsInt

		// Log provided limit
		commons.LogWithContext(ginctx, zerolog.InfoLevel, "Limit provided: "+limitAsString)
	}

	return limit, nil
}

func (ListHandler) createResponseDto(
	entities *[]entities.Entity,
) *dto.ResponseDto {

	values := []Value{}
	for _, entity := range *entities {
		values = append(values, Value{
			Id:    entity.Id,
			Value: entity.Value,
			Tag:   entity.Tag,
		})
	}

	data := ListResponseDto{
		Value: &values,
	}
	return &dto.ResponseDto{
		Message: "Values are retrieved successfully.",
		Data:    data,
	}
}
