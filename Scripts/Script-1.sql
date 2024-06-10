SELECT categories.id AS categoryId,
       categories.name AS categoryName,
       DATE_FORMAT(learning_data.month, '%Y-%m') AS month, 
       SUM(learning_data.study_time) AS totalStudyTime
FROM learning_data
JOIN categories ON learning_data.category_id = categories.id
GROUP BY categories.id, categories.name, DATE_FORMAT(learning_data.month, '%Y-%m')
ORDER BY DATE_FORMAT(learning_data.month, '%Y-%m') ASC;


