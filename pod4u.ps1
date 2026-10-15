param([string]$DefaultDistro = "", [string]$Username = (& (Get-Command $DefaultDistro).path --default-user), [string]$JFXModulePath = "")
$env:SPOTIFY_CLIENT_ID="changeme"
$env:SPOTIFY_SECRET="changeme"
$env:CODE_CHALLENGE="changeme"
$env:CODE_VERIFIER="changeme"
$env:FACE_PLUS_API_KEY="NVV3iKMu16Ea306RzqvPStvO6py4ZGHw"
$env:FACE_PLUS_API_SECRET="ND_ejH8jr-pxzA5a-EoRFGiSboWSzkMy"
$env:ACOUSTICBRAINZ_TOKEN="UfWt7DATnmK9md2QX0hEDiY94Hc4OlPm8vI3tLUf"
${X-RAPIDAPI-KEY}="692408644cmshba4f7571859d635p1b71fbjsnfcc4b12e318b"
Set-Location .\musicbrainz
wsl -u root -d $DefaultDistro bash -c "apt update; apt upgrade -y; apt install -y dos2unix; dos2unix mb.sh"
wsl -u $Username -d $DefaultDistro bash -c "chmod +x mb.sh; ./mb.sh $Username"
wsl -u root -d $DefaultDistro bash -c "apt update; apt install -y containerd.io docker-ce docker-ce-cli docker-ce-rootless-extras --fix-missing; chown -R $Username /var/run/docker.sock"
wsl -u $Username -d $DefaultDistro bash -c "docker-volume-snapshot restore ./pgdata.tar.bz2 pgdata; /home/$Username/bin/docker load -i musicbrainz.tar; /home/$Username/bin/docker rm -f mb; /home/$Username/bin/docker buildx build . -t mb:latest; /home/$Username/bin/docker run -it --name mb -d -h localhost -p 5432:5432 -e POSTGRES_USER=musicbrainz -e POSTGRES_PASSWORD=musicbrainz -e POSTGRES_DB=musicbrainz_db -v pgdata:/var/lib/postgresql mb:latest; rm xaa xab xac xad xae xaf xag xah xai ./pgdata.tar.bz2"
Set-Location ..\
.\gradlew.bat clean build --no-build-cache --refresh-dependencies
javac -p "C:\Users\Brian\Downloads\openjfx-21.0.2_windows-x64_bin-sdk\javafx-sdk-21.0.2\lib" -cp ".\src\main\java;.\src\main\resources;.\jackson-annotations-2.21.jar;.\jackson-core-3.1.3.jar;.\jackson-databind-3.1.3.jar" -d .\out --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base .\src\main\java\org\pod4u\app\Main.java
javaw.exe -p 'C:\Users\Brian\Downloads\openjfx-21.0.2_windows-x64_bin-sdk\javafx-sdk-21.0.2\lib' -cp '.\build\classes\java\main;.\src\main\java;.\src\main\resources;.\out;.\jackson-annotations-2.21.jar;.\jackson-core-3.1.3.jar;.\jackson-databind-3.1.3.jar' --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base org.pod4u.app.Main
wsl -u $Username -d $DefaultDistro bash -c "chmod +x ./musicbrainz/wait.sh; dos2unix ./musicbrainz/wait.sh; ./musicbrainz/wait.sh $Username"
