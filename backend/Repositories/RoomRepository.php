<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:06
 */

namespace Repositories;

use Utilities\Utitilies;

class RoomRepository{

    public static function getTalks(){
        $sql_query = "SELECT * FROM room";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll();
    }

}