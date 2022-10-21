package dto

type ResponseDto struct {
	Message string      `json:"message"`
	Data    interface{} `json:"data"`
}
