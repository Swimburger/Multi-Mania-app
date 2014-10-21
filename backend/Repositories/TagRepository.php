<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:08
 */

namespace Repositories;

use Utilities\Utitilies;

class TagRepository{

    /**
     * @return array
     */
    public static function getTags(){
    $sql_query = "SELECT * FROM tag";
    $con=Utitilies::getConnection();
    $stmt   = $con->query($sql_query);
    return $stmt->fetchAll(\PDO::FETCH_ASSOC);
}

}