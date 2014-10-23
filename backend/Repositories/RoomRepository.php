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

    /**
     * @return array
     */
    public static function getRooms(){
        $sql_query = "SELECT * FROM room";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

}