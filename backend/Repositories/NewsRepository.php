<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utitilies;

class NewsRepository {
    /**
     * @return array
     */
    public static function  getNews()
    {
        $sql_query = "SELECT * FROM news";
        $con=Utitilies::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    public static function insertNewsItem($importance, $order, $title, $shortDescription, $longDescription, $image)
    {
        $sql_query = "INSERT INTO news (title,img,short_description,long_description,importance,`order`) VALUES (:title,:img,:short_description,:long_description,:importance,:order);";
        $con = Utitilies::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':title'=>$title,':img'=>$image,':short_description'=>$shortDescription,':long_description'=>$longDescription,':importance'=>$importance,':order'=>$order));
    }
} 