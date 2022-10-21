using bravo_proxy_service.Services.Persistence;
using bravo_proxy_service.Services.Persistence.Handlers.Create.Dtos;
using Microsoft.AspNetCore.Mvc;

namespace bravo_proxy_service.Controllers;

[ApiController]
[Route("[controller]")]
public class PersistenceController
{
    private readonly ILogger<PersistenceController> _logger;
    private readonly IPersistenceService _persistenceService;

    public PersistenceController(
        ILogger<PersistenceController> logger,
        IPersistenceService persistenceService
    )
    {
        _logger = logger;
        _persistenceService = persistenceService;
    }

    [HttpPost(Name = "CreateValue")]
    [Route("create")]
    public async Task<IActionResult> Create(
        [FromBody] CreateValueRequestDto requestDto
    )
    {
        _logger.LogInformation("CreateValue endpoint is triggered...");

        var responseDto = await _persistenceService.Create(requestDto);

        return new CreatedResult($"{responseDto.Data.Value.Id}", responseDto);
    }

    [HttpGet(Name = "ListValues")]
    [Route("list")]
    public async Task<IActionResult> List(
        [FromQuery] int? limit
    )
    {
        _logger.LogInformation("ListValues endpoint is triggered...");

        var responseDto = await _persistenceService.List(limit);

        return new OkObjectResult(responseDto);
    }

    [HttpDelete(Name = "DeleteValue")]
    [Route("delete")]
    public async Task<IActionResult> Delete(
        [FromQuery] string id
    )
    {
        _logger.LogInformation("DeleteValue endpoint is triggered...");

        var responseDto = _persistenceService.Delete(id);

        return new OkObjectResult(responseDto);
    }
}
