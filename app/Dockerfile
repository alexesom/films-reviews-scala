FROM 1science/sbt:0.13.8-oracle-jre-8
RUN mkdir -p /app
WORKDIR /app
COPY . /app
EXPOSE 8080
CMD ["sbt", "flywayMigrate"]


