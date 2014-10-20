<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 17:57
 */

namespace Utilities;


class Params {
    const PRODUCTION=false;
    public static function getConnectionParams(){
        if(Params::PRODUCTION){
            return array("string"=>"mysql:host=student.howest.be/axel.jonckheere/mysqlstudent;dbname=axeljonckheere",
                "username"=>"axeljonckheere",
                "pwd"=>"Apo8aisool0j");
        }else{
            return array("string"=>"mysql:host=localhost;dbname=multimania",
                "username"=>"root",
                "pwd"=>"root");
        }
    }
} 