<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utilities;

/**
 * The repository contains all methods for interacting with the database for the News model
 *
 * Class NewsRepository
 * @package Repositories
 */
class NewsRepository {
    /**
     * @return array Returns all news
     */
    public static function  getNews()
    {
        $sql_query = "SELECT * FROM news";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $importance int The importance of the NewsItem
     * @param $order int The order of the NewsItem
     * @param $title string The title of the NewsItem
     * @param $shortDescription string The short description of the NewsItem
     * @param $longDescription string The long description of the NewsItem
     * @param $image string The url pointing to the image of the NewsItem
     * @return bool Returns true if item is inserted, else false
     */
    public static function insertNewsItem($importance, $order, $title, $shortDescription, $longDescription, $image)
    {
        $sql_query = "INSERT INTO news (title,img,short_description,long_description,importance,`order`) VALUES (:title,:img,:short_description,:long_description,:importance,:order);";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':title'=>$title,':img'=>$image,':short_description'=>$shortDescription,':long_description'=>$longDescription,':importance'=>$importance,':order'=>$order));
    }
} 