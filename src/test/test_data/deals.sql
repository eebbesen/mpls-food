INSERT INTO public.deals (id,date_created,description,last_updated,place_id,cuisine,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified) VALUES
	 (55,'2022-07-27 23:46:01.33711','$13.45 mushroom swiss burger, fries and a 20oz fountain drink','2022-10-05 08:48:02.339957',10004,'American','Burger',NULL,'admin',1.85,12.09,13.45,1.85,12.09,13.45,false,true),
	 (66,'2022-08-10 17:11:39.566796','$1.00 off Meat and Potato Burrito ($5.21 tax included)','2022-09-14 21:34:11.497607',2,'Tex Mex','Burrito',NULL,'admin',1.0,NULL,5.21,1.0,NULL,5.21,true,true),
	 (56,'2022-07-27 23:46:38.15725','$13.45 Minnesota burger, fries and a 20oz fountain drink','2022-10-05 08:49:21.406732',10004,'American','Burger',NULL,'admin',1.85,12.09,13.45,1.85,12.09,13.45,false,true),
	 (65,'2022-07-27 23:16:57.658109','$1.00 off Taco Bravo','2022-09-14 21:34:51.822252',2,'Tex Mex','Taco',NULL,'admin',1.0,NULL,NULL,1.0,NULL,NULL,false,true),
	 (53,'2022-07-27 23:42:17.849534','$12.45 cajun cheese burger, fries and a 20oz fountain drink','2022-10-05 08:53:28.760934',10004,'American','Burger',NULL,'admin',1.55,11.07,12.45,1.55,11.07,12.45,false,true),
	 (67,'2022-08-10 17:23:55.716542','$1.00 off taco salad','2022-09-14 16:07:42.187979',2,'Tex Mex','Taco',NULL,'admin',1.0,NULL,NULL,1.0,NULL,NULL,false,true),
	 (52,'2022-07-27 23:41:12.97514','$13.60 cheese burger, fries and a 20oz fountain drink','2022-10-05 08:54:15.238311',10004,'American','Burger',NULL,'admin',1.65,12.13,13.6,1.65,12.13,13.6,false,true),
	 (57,'2022-07-27 23:56:35.586779','Buy one hot dog get another half off','2022-10-05 14:36:34.804231',10011,'American','Hot Dog',NULL,'admin',1.83,25.0,5.48,1.22,25.0,3.68,false,true),
	 (63,'2022-07-28 00:03:39.268623','$3.00 (tax included) for any flavor small malt or root beer float.','2022-09-15 05:35:12.126051',10011,'American','Beverage',NULL,'admin',NULL,NULL,3.0,NULL,NULL,3.0,true,false),
	 (59,'2022-07-27 23:59:01.748955','Two Ball Park hot dogs for $4.50','2022-10-05 14:38:58.381768',10011,'American','Hot Dog',NULL,'admin',0.4,8.16,4.5,0.4,8.16,4.5,false,true);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,cuisine,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified) VALUES
	 (60,'2022-07-27 23:59:20.578794','Two Ball Park chili dogs for $5.50','2022-10-05 14:39:43.837407',10011,'American','Hot Dog',NULL,'admin',0.4,6.78,5.5,0.4,6.78,5.5,false,true),
	 (54,'2022-07-27 23:45:21.760957','$13.75 bacon cheese burger, fries and a 20oz fountain drink','2022-10-05 19:58:15.334742',10004,'American','Burger',NULL,'admin',1.55,10.13,13.75,1.55,10.13,13.75,false,true),
	 (58,'2022-07-27 23:16:36.873815','Half-price crispy tacos (Taco Tuesday). $1.19 pre-tax, $1.32 tax included.','2022-09-20 13:27:39.793612',2,'Tex Mex','Taco',NULL,'admin',1.19,50.0,1.19,1.19,50.0,1.19,false,true),
	 (68,'2022-08-10 17:24:21.696795','$1.39 softshell taco','2022-09-25 14:48:44.871546',2,'Tex Mex','Taco',NULL,'admin',NULL,NULL,1.39,NULL,NULL,1.39,false,true),
	 (64,'2022-07-28 00:14:24.973642','$5.00 for two slices from 10:30 - 11:00.','2022-09-25 15:40:57.541839',10000,'Italian','Pizza',NULL,'admin',3.9,43.82,5.0,2.4,32.43,5.0,false,false),
	 (119,'2022-10-09 14:44:00.985183','$1.00 off tacos 3:00 pm to 6:00 pm','2022-10-09 14:44:00.985183',29,'Mexican','Taco','admin','admin',1.0,18.18,6.0,1.0,14.29,4.5,false,false),
	 (116,'2022-10-09 14:10:01.764314','$7 Tacos, Drumsticks, A Ton of Taters from 3:00 pm to 6:00 pm','2022-10-09 14:56:33.839117',17,'Other','Appetizer','admin','admin',1.0,12.5,7.0,0.0,0.0,7.0,false,false),
	 (120,'2022-10-09 14:45:18.880189','$7 margaritas from 3:00 pm to 6:00 pm','2022-10-09 14:57:05.227078',29,'Mexican','Beverage','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,false),
	 (118,'2022-10-09 14:14:43.54359','From 3:00 pm to 6:00 pm:
$4 House Draft Beers,
$1 Off All Other Draft Beers,
$5 Single Premium Rail Pours,
$5 Glasses of House Wine,
$2 Off All Other Wine','2022-10-09 14:57:23.756525',17,'Other','Beverage','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,false),
	 (110,'2022-09-14 14:30:16.881066','2 for 1 garlic cheese bread','2022-09-14 21:34:39.290629',10000,'Italian','Bread','admin','admin',2.4,50.0,2.4,2.4,50.0,2.4,false,true);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,cuisine,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified) VALUES
	 (109,'2022-09-14 14:24:09.302423','12 chicken wings for $7.25','2022-09-14 21:36:11.759125',10000,'American','Chicken Wings','admin','admin',4.75,39.58,7.25,4.75,39.58,7.25,false,true),
	 (50,'2022-07-27 13:15:04.809267','$3.20 slices from 10:30 - 11:30.','2022-09-14 21:50:47.654027',10000,'Italian','Pizza',NULL,'admin',1.25,28.08,3.2,0.5,15.63,3.2,false,true),
	 (108,'2022-09-14 14:22:38.712614','Free breadsticks with purchase of a slice.','2022-09-15 05:29:58.370784',10000,'Italian','Bread','admin','admin',1.6,30.19,4.45,1.6,26.45,3.7,false,true),
	 (62,'2022-07-28 00:01:23.103103','$4.55 for a Ball Park hot dog with chips and a 24 ounce fountain drink.
$2.25 to add a dog.','2022-10-05 14:19:45.562252',10011,'American','Hot Dog',NULL,'admin',0.85,12.5,6.8,0.65,11.11,4.55,false,true),
	 (61,'2022-07-28 00:00:17.810187','$4.95 for a Vienna Beef hot dog with chips and a 24 ounce fountain drink.
$2.75 to add a beef hot dog.','2022-10-05 14:44:20.645028',10011,'American','Hot Dog',NULL,'admin',0.95,12.71,7.7,0.75,10.98,4.95,false,true),
	 (115,'2022-10-09 14:07:22.304616','$6 Pretzel Bites, Chips & Cheese, Garlic Parmesan Bites from 3:00 pm to 6:00 pm','2022-10-09 14:07:22.304616',17,'American','Appetizer','admin','admin',NULL,NULL,6.0,NULL,NULL,6.0,false,false),
	 (117,'2022-10-09 14:12:25.806187','$8 9" Solo 1-Topping Pizza, Wisconsin Curds, Onion Rings from 3:00 pm to 6:00 pm','2022-10-09 14:15:36.10378',17,'American','Appetizer','admin','admin',0.95,11.11,8.0,0.0,0.0,8.0,false,false);
