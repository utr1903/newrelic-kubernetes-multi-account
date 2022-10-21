using System.Net;
using Newtonsoft.Json;

namespace bravo_proxy_service.Dtos;

public class ResponseDto<T>
{
    [JsonProperty("message")]
    public string? Message { get; set; }

    [JsonProperty("statusCode")]
    public HttpStatusCode StatusCode { get; set; }

    [JsonProperty("data")]
    public T? Data { get; set; }
}

