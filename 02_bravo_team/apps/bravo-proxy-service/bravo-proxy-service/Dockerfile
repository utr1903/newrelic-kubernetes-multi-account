# syntax=docker/dockerfile:1
FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build-env
WORKDIR /app

# Copy csproj and restore as distinct layers
COPY *.csproj ./
RUN dotnet restore

# Copy everything else and build
COPY . ./
RUN dotnet publish -c Release -o out

# Build runtime image
FROM mcr.microsoft.com/dotnet/aspnet:6.0
WORKDIR /app
COPY --from=build-env /app/out .

# Enable the agent
ARG newRelicAppName
ARG newRelicLicenseKey

ENV NEW_RELIC_APP_NAME=$newRelicAppName
ENV NEW_RELIC_LICENSE_KEY=$newRelicLicenseKey

ENV NEW_RELIC_DISTRIBUTED_TRACING_ENABLED=true
ENV NEWRELIC_LOG_LEVEL=all
ENV NEW_RELIC_APPLICATION_LOGGING_ENABLED=true
ENV NEW_RELIC_APPLICATION_LOGGING_METRICS_ENABLED=true
ENV NEW_RELIC_APPLICATION_LOGGING_FORWARDING_ENABLED=true
ENV NEW_RELIC_APPLICATION_LOGGING_FORWARDING_MAX_SAMPLES_STORED=10000

ENV CORECLR_ENABLE_PROFILING=1
ENV CORECLR_PROFILER={36032161-FFC0-4B61-B559-F6C5D41BAE5A}
ENV CORECLR_NEWRELIC_HOME=/app/newrelic
ENV CORECLR_PROFILER_PATH=/app/newrelic/libNewRelicProfiler.so

EXPOSE 8080
ENTRYPOINT ["dotnet", "bravo-proxy-service.dll"]