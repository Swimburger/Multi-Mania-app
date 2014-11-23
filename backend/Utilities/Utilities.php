<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 17:48
 */

namespace Utilities;


class Utilities {
    public static function getConnection(){
        $params = Params::getConnectionParams();
        return Utilities::getConnectionFromParams($params['string'],$params['username'],$params['pwd']);
    }

    private static function getConnectionFromParams($connectionString, $userName,$pwd) {
        $conn = new \PDO($connectionString, $userName,$pwd);
        $conn->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);
        //$conn->setAttribute(\PDO::ATTR_EMULATE_PREPARES, false);
        return $conn;
    }
} 