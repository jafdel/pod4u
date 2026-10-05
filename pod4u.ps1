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
wsl -u root -d $DefaultDistro bash -c "apt update; apt upgrade -y; apt install -y dos2unix; dos2unix mb.sh"
wsl -u $Username -d $DefaultDistro bash -c "chmod +x mb.sh; ./mb.sh $Username"
wsl -u root -d $DefaultDistro bash -c "apt update; apt install -y containerd.io docker-ce docker-ce-cli docker-ce-rootless-extras --fix-missing; /home/$Username/bin/docker rm -f mb 2>/dev/null; /home/$Username/bin/docker load -i musicbrainz.tar; chown -R $Username /var/run/docker.sock"
wsl -u $Username -d $DefaultDistro bash -c "docker-volume-snapshot restore ./pgdata.tar.bz2 pgdata; /home/$Username/bin/docker buildx build . -t mb:latest; /home/$Username/bin/docker run -it --name mb -d -h localhost -p 5432:5432 -e POSTGRES_USER=musicbrainz -e POSTGRES_PASSWORD=musicbrainz -e POSTGRES_DB=musicbrainz_db -v pgdata:/var/lib/postgresql mb:latest; rm xaa xab xac xad xae xaf xag xah xai ./pgdata.tar.bz2"
Set-Location ..\
.\gradlew.bat clean build --no-build-cache
javac -p $JFXModulePath -cp ".\src\main\java;.\src\main\resources" -d .\out --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base .\src\main\java\org\pod4u\app\Main.java
javaw.exe -p $JFXModulePath -cp ".\build\classes\java\main;.\src\main\java;.\src\main\resources;.\out" --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base org.pod4u.app.Main
wsl -u $Username -d $DefaultDistro bash -c 'chmod +x ./musicbrainz/wait.sh; dos2unix ./musicbrainz/wait.sh; ./musicbrainz/wait.sh $Username'
