param([string]$DefaultDistro = "", [string]$Username = (& (Get-Command $DefaultDistro).path --default-user), [string]$ModulePath = "")
$MBDir = "./musicbrainz"
wsl -u $Username -d $DefaultDistro bash -c "cd ./musicbrainz; chmod +x ./mb.sh; ./mb.sh $Username"
wsl docker run -it --name mb -d -h localhost -p 5432:5432 -e POSTGRES_USER=musicbrainz -e POSTGRES_PASSWORD=musicbrainz -e POSTGRES_DB=musicbrainz_db -v pgdata:/var/lib/postgresql mb:latest
Start-Process powershell -ArgumentList '-NoExit', '-Command', "bash -c 'cd ./musicbrainz && chmod +x ./wait.sh && ./wait.sh $Username'"
.\gradlew clean build --no-build-cache
javac -p $ModulePath  -cp ".\src\main\java;.\src\main\resources" -d .\out --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base .\src\main\java\org\pod4u\app\Main.java
java -p $ModulePath -cp ".\build\classes\java\main;.\src\main\java;.\src\main\resources;.\out" --add-modules javafx.graphics,javafx.fxml,javafx.controls,java.base org.pod4u.app.Main