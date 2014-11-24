<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 17:48
 */

namespace Utilities;

/**
 * Contains methods for getting a PDO
 *
 * Class Utilities
 * @package Utilities
 */
class Utilities {
    /**
     * @return \PDO Return a PDO instance for the production db or debug db
     */
    public static function getConnection(){
        $params = Params::getConnectionParams();
        return Utilities::getConnectionFromParams($params['string'],$params['username'],$params['pwd']);
    }

    /**
     * @param $connectionString string The connectionstring for the PDO
     * @param $userName string The username to connect to the db
     * @param $pwd string The password to connect to the db
     * @return \PDO Returns the PDO connection
     */
    private static function getConnectionFromParams($connectionString, $userName,$pwd) {
        $conn = new \PDO($connectionString, $userName,$pwd);
        $conn->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);
        //$conn->setAttribute(\PDO::ATTR_EMULATE_PREPARES, false);
        return $conn;
    }
} 