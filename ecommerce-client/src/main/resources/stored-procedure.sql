/* Procedure structure for procedure `EZEE_SP_PRODUCT_IUD` */

/*!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_PRODUCT_IUD` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_PRODUCT_IUD`(
    IN pcrCode VARCHAR(30),
    IN pcrName VARCHAR(50),
    IN pcrDescription VARCHAR(300),
    IN pdcPrice DECIMAL(12,2),
    IN pcrBrandCode VARCHAR(30),
    IN pcrCategoryCode VARCHAR(30),
    IN pcrNamespaceCode VARCHAR(30),
    IN pitActiveFlag TINYINT,
    OUT pitRowCount INT
)
BEGIN

/*
*----------------------------------------------------------------------------------------------------
* Variable Initialization
*----------------------------------------------------------------------------------------------------
*/

    SET pitRowCount = 0;

/*
*----------------------------------------------------------------------------------------------------
* Check Whether Product Exists
*----------------------------------------------------------------------------------------------------
*/

    IF EXISTS (SELECT 1 FROM `products` WHERE `code` = pcrCode) THEN

/*
*----------------------------------------------------------------------------------------------------
* UPDATE PRODUCT
*----------------------------------------------------------------------------------------------------
*/

        UPDATE `products`
        SET
            `name` = pcrName,
            `description` = pcrDescription,
            `price` = pdcPrice,
            `brand_code` = pcrBrandCode,
            `category_code` = pcrCategoryCode,
            `namespace_code` = pcrNamespaceCode,
            `active_flag` = pitActiveFlag,
            `updated_at` = NOW()
        WHERE `code` = pcrCode;

        SELECT ROW_COUNT() INTO pitRowCount;

    ELSE

/*
*----------------------------------------------------------------------------------------------------
* INSERT PRODUCT
*----------------------------------------------------------------------------------------------------
*/

        INSERT INTO `products`
        (
            `code`,
            `name`,
            `description`,
            `price`,
            `brand_code`,
            `category_code`,
            `namespace_code`,
            `active_flag`,
            `updated_at`
        )
        VALUES
        (
            pcrCode,
            pcrName,
            pcrDescription,
            pdcPrice,
            pcrBrandCode,
            pcrCategoryCode,
            pcrNamespaceCode,
            pitActiveFlag,
            NOW()
        );

        SELECT ROW_COUNT() INTO pitRowCount;

    END IF;

END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
