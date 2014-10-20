<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:00
 */

namespace Repositories;

use Utilities\Utitilies;

class TalkRepository{

    public static function getTalks(){
        $sql_query = "SELECT * FROM talk";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll();
    }


}