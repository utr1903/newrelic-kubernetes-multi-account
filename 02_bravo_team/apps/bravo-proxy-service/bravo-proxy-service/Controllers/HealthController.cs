using System.Net;
using bravo_proxy_service.Dtos;
using Microsoft.AspNetCore.Mvc;

namespace bravo_proxy_service.Controllers;

[ApiController]
[Route("[controller]")]
public class HealthController : ControllerBase
{
    public HealthController() {

    }

    [HttpGet(Name = "HealthCheck")]
    public OkObjectResult CheckHealth()
    {
        var responseDto = new ResponseDto<string>
        {
            Message = "OK",
            StatusCode = HttpStatusCode.OK,
        };

        return new OkObjectResult(responseDto);
    }
}

