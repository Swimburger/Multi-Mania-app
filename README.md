# Multi-Mania #

## Introduction ##

The Multi-Mania project is a backend and application for the [Multi-Mania conference](http://multi-mania.be/).

The multimania project exists out of 3 big parts.

 1. The backend
 2. Wireframes for all major platforms (Android, iOS, Windows Phone 8, Windows Store)
 3. The android client

## Backend ##

The purpose of the backend is to provide a simple json api for the clients.  
There is no CMS included but you can import data with an XML file.
The backend is build in a typical MVC structure using the [SLIM framework](http://www.slimframework.com/).

### Set-up ###

#### Parameters ####
In the params class you can set whether you are in production mode or not.  
If in production the production db will be returned from getConnectionParams().  
If in production the app will not run in debug mode and give safe error messages.  

    namespace Utilities;

    class Params {
        const PRODUCTION=false;

        public static function getConnectionParams(){
            if(Params::PRODUCTION){
                return array("string"=>"mysql:dbname=productiondb;host=productionhost",
                    "username"=>"productionuser",
                    "pwd"=>"productionpwd");
            }else{
                return array("string"=>"mysql:host=localhost;dbname=multimania",
                    "username"=>"root",
                    "pwd"=>"root");
            }
        }
    } 

This file is hidden for safety purposes.

#### Database ####

There is a sql query provided to reproduce the database.  
You can find it at backend/createTables.sql.

#### Data importing ####

You can import data into the database manually or you can make use of the import functionality included in the app.  
There is an xml file under backend/data.xml that will be processed and will update the data in the backend if you browse to the /import path.
Please beware that this /import path should be removed when you are done importing to ensure nobody else can browse to it.  

   
For more info go to the [wiki](multi-mania/wiki/Backend) or for detailed documentation about the backend you can go to this generated PHP  [documentation](http://sniels.bitbucket.org/multi-mania/backend).

## Android ##

The android application supports all devices that have at least Ice Cream Sandwich 4.0 sdk 14 on it.  
We support phones, small tablets and tablets.

Features:

* Schedule overview
* Ability to favorite talks
    * You can get an overview in My Schedule
    * You get notified 10 minutes before start
* An indoor map to find the right rooms
* News

### Play Store ###

We have deployed an alpha apk to the play store.   
You can try and test the app out only if you are part of the google group or google plus community and opted in for it.   
Follow the links below to achieve alpha access.   

[Play store link](https://play.google.com/store/apps/details?id=be.ana.nmct.multimania)   
This link might not work as long as there is no production app and you are not in the beta or alpha program.

#### Alpha ####

* [Google group](https://groups.google.com/d/forum/multi-mania-android-alpha)
* [Google+ community](https://plus.google.com/communities/114637708619233859276)
* [Opt in for alpha](https://play.google.com/apps/testing/be.ana.nmct.multimania)

#### Beta ####

To come

### Set-up ###

The application is build with Android Studio and makes use of the gradle plug-in.  
In the build.gradle app file you can set the url that you're api is hosted on.

    defaultConfig {
            ...
            buildConfigField "String", "API_URL", '"http://student.howest.be/niels.swimberghe/multimania/"'
        }
        buildTypes {
            release {
                ...
                buildConfigField "String", "API_URL", '"http://student.howest.be/niels.swimberghe/multimania/"'
            }
        }
    }

For more details please visit the [wiki](wiki/Android) or the [javadocs](http://sniels.bitbucket.org/multi-mania/android/).  

## Contact ##

You can contact us with any questions regarding the setup and bugs at the following email addresses.

* [niels.swimberghe@student.howest.be](mailto:niels.swimberghe@student.howest.be)
* [axel.jonckheere@student.howest.be](mailto:axel.jonckheere@student.howest.be)
* [astrid.desmedt@student.howest.be](mailto:astrid.desmedt@student.howest.be)