<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:38
 */

namespace Repositories;

use Utilities\Utitilies;

class UserRepository{


    public static function insertUser(){
        $sql_query = "INSERT";          //todo make statement
        $con=Utitilies::getConnection();
    }

}