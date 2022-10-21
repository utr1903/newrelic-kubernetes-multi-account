using bravo_proxy_service.Services.Persistence;
using bravo_proxy_service.Services.Persistence.Handlers.Create;
using bravo_proxy_service.Services.Persistence.Handlers.Delete;
using bravo_proxy_service.Services.Persistence.Handlers.List;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddHttpClient();

builder.Services.AddScoped<ICreateValueHandler, CreateValueHandler>();
builder.Services.AddScoped<IListValueHandler, ListValueHandler>();
builder.Services.AddScoped<IDeleteValueHandler, DeleteValueHandler>();
builder.Services.AddScoped<IPersistenceService, PersistenceService>();

builder.Services.AddControllers();

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseAuthorization();

app.MapControllers();

app.Run("http://*:8080");
