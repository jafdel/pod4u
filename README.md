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

Open a terminal window (preferably PowerShell) and run:

`wsl --install -d <DistroName>`

You can see the names of available distros using:

`wsl --list --online`

#### b) Install Git in your distro

Make sure you are inside the Linux environment. If not already in it, simply using the `wsl` command will log the terminal window into your default distro.

To install Git, run:

`sudo apt install -y git`

This will install Git in your distro, allowing you to clone the pod4u repo.

### Step 3: Clone the repo

To obtain a local copy of pod4u, stored in the directory you specified earlier, use:

`git clone https://github.com/jafdel/pod4u`

### Step 4: Include requirements for Spotify API access

#### a) Add Spotify Developer keys

With the Client ID and Client Secret copied from your Spotify Developer dashboard, open the `.env` file in a text editor of your choice. Replace the `changeme` values used for `SPOTIFY_CLIENT_ID` and `SPOTIFY_SECRET` with your ID and secret.

#### b) Generate authorisation strings

Use a PKCE authorisation generator tool such as that found at https://www.authaction.com/tools/pkce-generator.html to generate a 128-character SHA-256 code verifier, and a shorter matching code challenge, and copy them into your text editor before **immediately closing the generator tool**.

#### c) Add authorisation strings

Copy both strings into the `.env` file, assigning them as values for the `CODE_CHALLENGE` and `CODE_VERIFIER` keys.

### Step 5 (?): Include credentials for other APIs used

_**Please note:** This step may be unnecessary as keys have been obtained and added to the `.env` file for the other APIs this app uses, using dummy accounts linked to (temporary) public-facing email addresses. You may, however, prefer to have your own accounts - or there may be issues with the ones used here - so the steps to create them will be covered below._



### Step 6: Launch the app



### Step 7: Use the app to generate a playlist



## Missing features (September 2026)

The following features are planned for future releases:

* Database for logging in/registering with pod4u, which will be used to save, load and share playlists
* Playback for final playlists - at present the functionality is mainly for the purposes of demonstrating how the playlists are created and what the results might look like
* Other means, besides photo uploads, of telling pod4u your mood (namely a direct camera interaction, a series of sliders in the UI and a randomiser function)
* Logout function (i.e. disconnecting from the Spotify account and returning to the home screen)
