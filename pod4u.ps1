param([string]$DefaultDistro = "", [string]$Username = (& (Get-Command $DefaultDistro).path --default-user), [string]$ModulePath = "")
Get-Content .env | ForEach-Object {
  $line = $_.Trim()
  if ($line -match '^\s*#') { return }
  if ($line -match '^\s*$') { return }
  if ($line -eq "" -or $line.StartsWith("#")) { return }
  $parts = $line.Split("=", 2)
  if ($parts.Count -ne 2) {
    Write-Warning "Skipping invalid line ($line) - there must be only one equals sign"
    return
  }
  $name = $parts[0].Trim()
  $value = $parts[1].Trim()
  [System.Environment]::SetEnvironmentVariable($name, $value)
}
wsl -u $Username -d $DefaultDistro bash -c "cd ./musicbrainz; chmod +x ./mb.sh; ./mb.sh $Username"
wsl docker run -it --name mb -d -h localhost -p 5432:5432 -e POSTGRES_USER=musicbrainz -e POSTGRES_PASSWORD=musicbrainz -e POSTGRES_DB=musicbrainz_db -v pgdata:/var/lib/postgresql mb:latest
Start-Process powershell -ArgumentList '-NoExit', '-Command', "bash -c 'cd ./musicbrainz && chmod +x ./wait.sh && ./wait.sh $Username'"
.\gradlew clean build --no-build-cache
javac -p $ModulePath  -cp ".\src\main\java;.\src\main\resources" -d .\out --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base .\src\main\java\org\pod4u\app\Main.java
java -p $ModulePath -cp ".\build\classes\java\main;.\src\main\java;.\src\main\resources;.\out" --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base org.pod4u.app.Main