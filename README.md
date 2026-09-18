# pod4u: Playlists to fit your mood

This is pod4u, a Java application that allows you to generate playlists from your liked songs on Spotify, based on your current mood.

## How to use

### Step 1: Ensure you have a Spotify Premium subscription (required)

_**Please note:** The Spotify Web API, a central part of this app, is **only available to Spotify Premium subscribers** (in September 2026) **who have signed up as a developer**. If you try to access it with an account currently on the Free plan, or an account that has not been registered as a developer account, it will return an error, saying that the request is forbidden._

To get started with Spotify for Developers and the Web API, visit https://developer.spotify.com/documentation/web-api and follow the 'Getting Started' instructions. Be sure to keep your Client ID and Client Secret somewhere safe.

### Step 2: Ensure your local environment is set up

_**Please note:** Thus far, the app has been run **exclusively** on a **Windows environment** with WSL2 installed. That being said, most, but not all of the PowerShell script (pod4u.ps1) is just Linux commands so it should be easy to port. Ultimately, this is a culmination of months of work so to at least have some version of it uploaded here matters to me, but if you would rather not wait until I get round to this then you are of course welcome to adapt the script for your own use within Linux or macOS._

To ensure you have the correct environment to run pod4u, please complete the following steps if you have not done them already:

#### a) Install WSL with a chosen Linux distribution

Open a terminal window (preferably PowerShell) and move to the directory where you intend to save pod4u by running:

`cd C:\choose\your\path`

Ideally this will not change again in this process. Now you can install your WSL environment. Run:

`wsl --install -d <DistroName>`

You can see the names of available distros using:

`wsl --list --online`

Take note of the name of the distribution as it will be needed later. You will also need your Linux username, which you should be prompted to enter after the distribution has been installed, along with a password.

#### b) Install Git in your distro

Make sure you are inside the Linux environment. If not already in it, simply using the `wsl` command will log the terminal window into your default distro.

To install Git, run:

`sudo apt install -y git`

This will install Git in your distro, allowing you to clone the pod4u repo.

#### c) Set up Java 25

If Java 25 is not installed on your machine, get it at [https://download.oracle.com/java/25/latest/jdk-25_windows-x64_bin.exe](https://download.oracle.com/java/25/latest/jdk-25_windows-x64_bin.exe) and execute it to go through the installation process.

#### d) Set up JavaFX (OpenJFX)

JavaFX is a technology on which pod4u is built, allowing your machine to run rich GUI applications in Java. It can be downloaded from [https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_windows-x64_bin-sdk.zip](https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_windows-x64_bin-sdk.zip). Extract the archive and note the location it is saved to as this will be needed later.

### Step 3: Clone the repo

To obtain a local copy of pod4u, stored in the directory you specified in PowerShell earlier, use:

`git clone https://github.com/jafdel/pod4u`

### Step 4: Include requirements for Spotify API access

#### a) Add Spotify Developer keys

With the Client ID and Client Secret copied from your Spotify Developer dashboard, open the `.env` file in a text editor of your choice. Replace the `changeme` values used for `SPOTIFY_CLIENT_ID` and `SPOTIFY_SECRET` with your ID and secret.

#### b) Generate authorisation strings

Use a PKCE authorisation generator tool such as that found at https://www.authaction.com/tools/pkce-generator.html to generate a 128-character SHA-256 code verifier, and a shorter matching code challenge, and copy them into your text editor before **immediately closing the generator tool**.

#### c) Add authorisation strings

Copy both strings into the `.env` file, assigning them as values for the `CODE_CHALLENGE` and `CODE_VERIFIER` keys, again replacing `changeme`.

### Step 5 (?): Include credentials for other APIs used

_**Please note:** This step may be unnecessary as keys have already been obtained and added to the `.env` file for the other APIs this app uses, created with dummy accounts linked to (temporary) public-facing email addresses. You may, however, prefer to have your own accounts - or there may be issues with the ones used here - so the steps to create them will be covered below._

#### a) Set up Face++ account

Go to https://console.faceplusplus.com/register and follow the instructions to create an account. Once signed in, there should be an 'Apps' option on the left-hand side, and 'API Keys' under it. On this page you will find the 'Get API Key' button, which will create a new key for you. Copy the API key and API secret into the `.env` file as values for the FACE_PLUS_API_KEY and FACE_PLUS_API_SECRET keys.

#### b) Set up AcousticBrainz account

Go to https://metabrainz.org/signup and follow the instructions to create an account. Once signed in, click your username in the top right-hand corner of the page, and then 'Your profile'. On this page you will find the 'Generate new key' button, which will create a new key for you. Copy the key into the `.env` file as the value for the ACOUSTICBRAINZ_TOKEN key.

#### c) Set up RapidAPI account



### Step 6: Launch the app

Back in PowerShell, within the same location you downloaded the repo to, launch pod4u by running:

`.\pod4u.ps1 -DefaultDistro <DistroName> -Username <Username> -ModulePath <JFXModulePath>`

DistroName is the name of your default distro, Username is the username you will use to log into the distro, and ModulePath is the directory where you extracted the OpenJFX archive file.

_**Please note:** This process will probably take a few hours to complete first time around as it involves self-hosting a copy of the MusicBrainz database in full._

### Step 7: Use the app to generate a playlist



## Missing features (September 2026)

The following features are planned for future releases:

* Database for logging in/registering with pod4u, which will be used to save, load and share playlists
* Playback for final playlists - at present the functionality is mainly for the purposes of demonstrating how the playlists are created and what the results might look like
* Other means, besides photo uploads, of telling pod4u your mood (namely a direct camera interaction, a series of sliders in the UI and a randomiser function)
* Logout function (i.e. disconnecting from the Spotify account and returning to the home screen)
