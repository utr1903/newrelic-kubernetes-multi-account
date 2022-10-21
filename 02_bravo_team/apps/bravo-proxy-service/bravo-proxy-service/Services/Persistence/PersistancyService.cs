using System.Net;
using bravo_proxy_service.Dtos;
using bravo_proxy_service.Services.Persistence.Handlers.Create;
using bravo_proxy_service.Services.Persistence.Handlers.Create.Dtos;
using bravo_proxy_service.Services.Persistence.Handlers.Delete;
using bravo_proxy_service.Services.Persistence.Handlers.List;
using bravo_proxy_service.Services.Persistence.Handlers.List.Dtos;

namespace bravo_proxy_service.Services.Persistence;

public interface IPersistenceService
{
    Task<ResponseDto<CreateValueResponseDto>> Create(
        CreateValueRequestDto requestDto
    );

    Task<ResponseDto<ListValueResponseDto>> List(
        int? limit
    );

    ResponseDto<string> Delete(
        string id
    );
}

public class PersistenceService : IPersistenceService
{
    private readonly ILogger<PersistenceService> _logger;

    private readonly ICreateValueHandler _createValueHandler;
    private readonly IListValueHandler _listValueHandler;
    private readonly IDeleteValueHandler _deleteValueHandler;

    public PersistenceService(
        ILogger<PersistenceService> logger,
        ICreateValueHandler createValueHandler,
        IListValueHandler listValueHandler,
        IDeleteValueHandler deleteValueHandler
    )
    {
        _logger = logger;
        _createValueHandler = createValueHandler;
        _listValueHandler = listValueHandler;
        _deleteValueHandler = deleteValueHandler;
    }

    public async Task<ResponseDto<CreateValueResponseDto>> Create(
        CreateValueRequestDto requestDto
    )
    {
        _logger.LogInformation("Creating value entity ...");

        // Send request to persistence service.
        var data = await _createValueHandler.Run(requestDto);

        // Create response DTO.
        var responseDto = new ResponseDto<CreateValueResponseDto>
        {
            Message = "Value is created successfully.",
            StatusCode = HttpStatusCode.Created,
            Data = data,
        };

        return responseDto;
    }

    public async Task<ResponseDto<ListValueResponseDto>> List(
        int? limit
    )
    {
        _logger.LogInformation("Retrieving value entities ...");

        // Send request to persistence service.
        var data = await _listValueHandler.Run(limit);

        // Create response DTO.
        var responseDto = new ResponseDto<ListValueResponseDto>
        {
            Message = "Values are retrieved successfully.",
            StatusCode = HttpStatusCode.OK,
            Data = data,
        };

        return responseDto;
    }

    public ResponseDto<string> Delete(
        string id
    )
    {
        _logger.LogInformation("Deleting value entity ...");

        // Send request to persistence service.
        _deleteValueHandler.Run(id);

        // Create response DTO.
        var responseDto = new ResponseDto<string>
        {
            Message = "Value is deleted successfully.",
            StatusCode = HttpStatusCode.OK,
        };

        return responseDto;
    }
}
