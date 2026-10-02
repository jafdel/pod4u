param([string]$DefaultDistro = "", [string]$Username = (& (Get-Command $DefaultDistro).path --default-user), [string]$JFXModulePath = "")
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
Set-Location .\musicbrainz
wsl -u root -d $DefaultDistro bash -c "sudo apt update; sudo apt upgrade -y; sudo apt install -y dos2unix; dos2unix mb.sh"
wsl -u $Username -d $DefaultDistro bash -c "chmod +x mb.sh; ./mb.sh $Username"
Set-Location ..\
.\gradlew.bat clean build --no-build-cache
javac -p $JFXModulePath -cp ".\src\main\java;.\src\main\resources" -d .\out --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base .\src\main\java\org\pod4u\app\Main.java
javaw.exe -p $JFXModulePath -cp ".\build\classes\java\main;.\src\main\java;.\src\main\resources;.\out" --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base org.pod4u.app.Main
wsl -d $DefaultDistro -u $Username /bin/bash -c 'chmod +x ./musicbrainz/wait.sh; dos2unix ./musicbrainz/wait.sh'
wsl -d $DefaultDistro /bin/bash -c musicbrainz/wait.sh $Username
