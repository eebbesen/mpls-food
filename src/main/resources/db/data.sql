INSERT INTO public.users (id,username,"password",enabled) VALUES
	 (1,'user','$2a$04$vfJTzvsrzJc1AsOdgjb3ge3TX1ZUTeAJa3TIS8Yzqen4t6NMx7xvm',true),
	 (2,'admin','$2a$04$vfJTzvsrzJc1AsOdgjb3ge3TX1ZUTeAJa3TIS8Yzqen4t6NMx7xvm',true);

INSERT INTO public.authorities (id,username,authority) VALUES
	 (1, 'user','ROLE_USER'),
	 (2, 'admin','ROLE_USER'),
	 (3, 'admin','ROLE_ADMIN');

INSERT INTO public.places (id,address,app,date_created,last_updated,"name",order_ahead,website,created_by,modified_by) VALUES
	 (27,'1260 Hennepin Ave
Minneapolis, MN 55403',true,'2022-10-09 14:32:48.968892','2022-10-09 14:32:48.968892','Subway Hennepin',true,'https://restaurants.subway.com/united-states/mn/minneapolis/1260-hennepin-ave','admin','admin'),
	 (10004,'220 South 6th St #215
Minneapolis, MN 55402',false,'2022-07-27 23:38:35.619756','2022-07-27 23:38:35.619756','The Burger Place',true,'https://www.theburgerplace.com/weekday-specials/',NULL,NULL),
	 (10011,'618 2nd Ave S Ste 114
Minneapolis, MN 55402',false,'2022-07-27 23:55:33.513632','2022-07-27 23:55:33.513632','Walkin''dog',false,'https://www.facebook.com/Walkindog-126087764097684/',NULL,NULL),
	 (29,'925 Nicollet Mall
Minneapolis, MN 55402',false,'2022-10-09 14:40:43.014166','2022-10-09 14:40:43.014166','Barrio',true,'https://barriotequila.com/locations/downtown-minneapolis/','admin','admin'),
	 (30,'225 S 6th St. Capella Tower',false,'2022-10-11 09:01:07.31082','2022-10-11 09:01:07.31082','Andrea Pizza Capella Tower',false,'https://www.andreapizza.com/','admin','admin'),
	 (31,'811 LaSalle Avenue Highland Court Building',false,'2022-10-11 09:01:56.454067','2022-10-11 09:01:56.454067','Andrea Pizza LaSalle',false,'https://www.andreapizza.com/','admin','admin'),
	 (32,'330 2nd Avenue S. #258',false,'2022-10-11 09:02:22.061839','2022-10-11 09:02:22.061839','Andrea Pizza Towle',false,'https://www.andreapizza.com/','admin','admin'),
	 (16,'705 S Marquette Avenue
Minneapolis, MN 55402',false,'2022-10-05 15:24:55.311793','2022-10-09 13:36:57.850817','Afro Deli',true,'https://www.afrodeli.com/locations/skyway','admin','admin'),
	 (15,'200 South 6th St #270
Minneapolis, MN 55402',true,'2022-10-05 08:34:20.311449','2022-10-09 13:47:16.692923','Broadway Fast & Fresh',true,'https://www.broadwaypizza.com/fast-and-fresh/fast-and-fresh','admin','admin'),
	 (17,'212 7th St.
Minneapolis, MN 55402',true,'2022-10-05 17:06:06.68425','2022-10-09 13:48:24.655347','Dan Kelly''s Broadway Pizza',true,'https://www.broadwaypizza.com/dan-kellys/dan-kellys','admin','admin');
INSERT INTO public.places (id,address,app,date_created,last_updated,"name",order_ahead,website,created_by,modified_by) VALUES
	 (10000,'121 S 8th Street #235
Minneapolis, MN 55402',false,'2022-07-27 13:09:06.542326','2022-10-09 13:48:55.803837','Ginelli''s Pizza',false,'https://www.ginellispizza.com/',NULL,'admin'),
	 (34,'210 IDS Center 80 8th S
Minneapolis, MN 55402',true,'2022-10-12 23:02:05.526777','2022-10-12 23:02:05.526777','Potbelly IDS',true,'https://www.potbelly.com/perks','admin','admin'),
	 (2,'607 Marquette Ave.
Minneapolis, MN 55402',true,'2022-07-27 22:00:30.162734','2022-10-09 13:52:12.043233','Taco John''s',false,'https://locations.tacojohns.com/mn/minneapolis/607-marquette-ave/',NULL,'admin'),
	 (23,'50 South 9th St.
Minneapolis, MN 55402',true,'2022-10-09 14:29:03.084173','2022-10-09 14:29:03.084173','Subway 9th St',true,'https://restaurants.subway.com/united-states/mn/minneapolis/50-south-9th-st','admin','admin'),
	 (24,'501 Washington Ave S
Suite 170
Minneapolis, MN 55415',true,'2022-10-09 14:30:09.36875','2022-10-09 14:30:09.36875','Subway Washington',true,'https://restaurants.subway.com/united-states/mn/minneapolis/501-washington-ave-s','admin','admin'),
	 (25,'701 4th Avenue South
Suite 230
Minneapolis, MN 55415',true,'2022-10-09 14:31:00.441405','2022-10-09 14:31:00.441405','Subway 4th Ave',true,'https://restaurants.subway.com/united-states/mn/minneapolis/701-4th-avenue-south','admin','admin'),
	 (26,'200 South 5th Street
Minneapolis, MN 55402',true,'2022-10-09 14:31:45.36618','2022-10-09 14:31:45.36618','Subway 5th St',true,'https://restaurants.subway.com/united-states/mn/minneapolis/200-south-5th-street','admin','admin'),
	 (44,'720 3rd Street South
Minneapolis, MN 55415',true,'2022-10-13 10:56:29.489237','2022-10-13 10:56:29.489237','Jimmy John''s #3836',true,'https://locations.jimmyjohns.com/mn/minneapolis/sandwiches-3836.html','admin','admin'),
	 (33,'527 S Marquette Ave
Minneapolis, MN 55402',true,'2022-10-12 23:00:59.643811','2022-10-12 23:03:17.04248','Potbelly Rand Tower',true,'https://www.potbelly.com/perks','admin','admin'),
	 (22,'555 Nicollet Mall Suite 297
Minneapolis, MN 55402',true,'2022-10-09 14:27:13.95516','2022-10-12 23:03:25.87861','Subway Nicollet',true,'https://restaurants.subway.com/united-states/mn/minneapolis/555-nicollet-mall','admin','admin');
INSERT INTO public.places (id,address,app,date_created,last_updated,"name",order_ahead,website,created_by,modified_by) VALUES
	 (40,'50 South 6th St Ste 240
Minneapolis, MN 55402',true,'2022-10-13 08:43:39.459999','2022-10-13 10:59:13.948588','Chipotle 50 South 6th',true,'https://locations.chipotle.com/mn/minneapolis/50-s-6th-st','admin','admin'),
	 (28,'931 Nicollet Mall
Minneapolis, MN 55402',false,'2022-10-09 14:38:56.14607','2022-10-12 23:07:30.687316','The Local',true,'https://the-local.com/pub-club/','admin','admin'),
	 (45,'200 South 6th St
Minneapolis, MN 55402',true,'2022-10-13 11:00:39.772978','2022-10-13 11:00:39.772978','Chipotle 200 South 6th',true,'https://chipotle.com/','admin','admin'),
	 (35,'200 South 6th St Ste 280
Minneapolis, MN 55402',false,'2022-10-12 23:51:31.57337','2022-10-12 23:59:11.282884','Jr. Brothers',true,'https://www.jrbrothersthai.com/order#signup','admin','admin'),
	 (36,'200 South 6th St
Minneapolis, MN 55402',true,'2022-10-13 07:41:07.693369','2022-10-13 07:41:07.693369','Naf Naf US Bank Plaza',true,'https://www.nafnafgrill.com/perks/','admin','admin'),
	 (37,'40 S 7th St #206
Minneapolis, MN 55402',true,'2022-10-13 07:42:38.42555','2022-10-13 07:42:38.42555','Naf Naf City Center',true,'https://www.nafnafgrill.com/perks/','admin','admin'),
	 (38,'651 Nicollet Mall
Minneapolis, MN 55402',true,'2022-10-13 08:03:17.723368','2022-10-13 08:03:17.723368','Freshii',true,'https://www.freshii.com/us/en-us/rewards','admin','admin'),
	 (46,'1040 Nicollet Mall
Minneapolis, MN 55403',true,'2022-10-13 11:01:14.102068','2022-10-13 11:01:14.102068','Chipotle Nicollet',true,'https://chipotle.com','admin','admin'),
	 (39,'40 S 7th St
Minneapolis, MN 55402',true,'2022-10-13 08:36:19.305232','2022-10-13 08:45:45.106076','Leeann Chin',true,'https://www.leeannchin.com/restaurant/city-centerdowntown-minneapolis-mn/40-south-7th-street','admin','admin'),
	 (41,'600 Hennepin Ave S, Ste 140
Minneapolis, MN 55403',true,'2022-10-13 10:52:25.956979','2022-10-13 10:52:25.956979','Jimmy John''s #191',true,'https://locations.jimmyjohns.com/mn/minneapolis/sandwiches-191.html','admin','admin');
INSERT INTO public.places (id,address,app,date_created,last_updated,"name",order_ahead,website,created_by,modified_by) VALUES
	 (42,'Pillsbury Center, 200 South 6th St
Minneapolis, MN 55402',true,'2022-10-13 10:53:38.577923','2022-10-13 10:53:38.577923','Jimmy John''s #192 Pillsbury',true,'https://locations.jimmyjohns.com/mn/minneapolis/sandwiches-192.html','admin','admin'),
	 (43,'88 S 9th St, Minneapolis
MN 55402',true,'2022-10-13 10:55:04.962617','2022-10-13 10:55:04.962617','Jimmy John''s #190',true,'https://locations.jimmyjohns.com/mn/minneapolis/sandwiches-190.html','admin','admin'),
	 (47,'50 South 10th St, Nicollet Mall
Minneapolis, MN 55403',true,'2022-10-13 11:18:58.565185','2022-10-13 11:18:58.565185','Qdoba',true,'https://locations.qdoba.com/us/mn/minneapolis/50-south-10th-st.html','admin','admin'),
	 (48,'809 Nicollet Mall
Minneapolis, MN 55402',true,'2022-10-13 11:33:56.752244','2022-10-13 11:33:56.752244','Panera Bread',true,'https://www.panerabread.com','admin','admin'),
	 (49,'601 S Marquette Ave
Minneapolis, MN 55402',false,'2022-11-20 16:08:33.677729','2022-11-20 16:08:33.677729','My Burger',true,'https://www.myburgerusa.com/','admin','admin');

INSERT INTO public.deals (id,date_created,description,last_updated,place_id,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified,end_time,start_time,end_date,start_date,deal_type) VALUES
	 (129,'2022-11-20 16:17:39.101783','Every Friday-Monday in November, try the Spicy Buffalo and Chipotle Cheddar for only $4.99, only on the freshii app and website','2022-11-20 16:22:57.88882',38,'Other','admin','admin',4.0,44.5,4.99,3.0,37.5,4.99,false,true,'15:00','10:00','2022-11-28','2022-11-04',0),
	 (123,'2022-10-11 09:07:57.094382','Get a free fountain drink when you buy 2 slices, or 1 slice and a salad','2022-12-27 16:00:51.79989',31,'Beverage','admin','admin',2.25,20.81,10.72,2.25,17.35,8.56,false,false,'15:30','10:00',NULL,NULL,3),
	 (121,'2022-10-11 09:05:49.967064','Get a free fountain drink when you buy 2 slices, or 1 slice and a salad','2022-12-27 16:00:59.483624',32,'Beverage','admin','admin',2.25,20.81,10.72,2.25,17.35,8.56,false,false,'15:00','10:00',NULL,NULL,3),
	 (119,'2022-10-09 14:44:00.985183','$1.00 off tacos 3:00 pm to 6:00 pm','2022-12-27 16:01:09.132827',29,'Taco','admin','admin',1.0,18.18,6.0,1.0,14.29,4.5,false,false,'18:00','15:00',NULL,NULL,3),
	 (120,'2022-10-09 14:45:18.880189','$7 margaritas from 3:00 pm to 6:00 pm','2022-12-27 16:01:16.380394',29,'Beverage','admin','admin',NULL,NULL,7.0,NULL,NULL,7.0,false,false,'18:00','15:00',NULL,NULL,3),
	 (115,'2022-10-09 14:07:22.304616','$6 Pretzel Bites, Chips & Cheese, Garlic Parmesan Bites from 3:00 pm to 6:00 pm','2022-12-27 16:01:27.947591',17,'Appetizer','admin','admin',NULL,NULL,6.0,NULL,NULL,6.0,false,false,'18:00','15:00',NULL,NULL,3),
	 (116,'2022-10-09 14:10:01.764314','$7 Tacos, Drumsticks, A Ton of Taters from 3:00 pm to 6:00 pm','2022-12-27 16:01:38.612517',17,'Appetizer','admin','admin',1.0,12.5,7.0,0.0,0.0,7.0,false,false,'18:00','15:00',NULL,NULL,3),
	 (117,'2022-10-09 14:12:25.806187','$8 9" Solo 1-Topping Pizza, Wisconsin Curds, Onion Rings from 3:00 pm to 6:00 pm','2022-12-27 16:02:03.988307',17,'Appetizer','admin','admin',0.95,11.11,8.0,0.0,0.0,8.0,false,false,'18:00','15:00',NULL,NULL,3),
	 (50,'2022-07-27 13:15:04.809267','$3.20 slices from 10:30 - 11:30.','2022-12-27 16:03:20.685801',10000,'Pizza',NULL,'admin',1.25,28.08,3.2,0.5,15.63,3.2,false,true,'11:30','10:30',NULL,NULL,3),
	 (58,'2022-07-27 23:16:36.873815','Half-price crispy tacos (Taco Tuesday). $1.19 pre-tax, $1.32 tax included.','2022-12-27 16:03:35.462581',2,'Taco',NULL,'admin',1.19,50.0,1.19,1.19,50.0,1.19,false,true,'17:00','10:30',NULL,NULL,3);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified,end_time,start_time,end_date,start_date,deal_type) VALUES
	 (53,'2022-07-27 23:42:17.849534','$12.45 cajun cheese burger, fries and a 20oz fountain drink','2022-12-27 16:03:44.769189',10004,'Burger',NULL,'admin',1.55,11.07,12.45,1.55,11.07,12.45,false,true,'15:00','10:30',NULL,NULL,3),
	 (61,'2022-07-28 00:00:17.810187','$4.95 for a Vienna Beef hot dog with chips and a 24 ounce fountain drink.
$2.75 to add a beef hot dog.','2022-12-27 16:03:54.9348',10011,'Hot Dog',NULL,'admin',0.95,12.71,7.7,0.75,10.98,4.95,false,true,'15:00','10:00',NULL,NULL,3),
	 (109,'2022-09-14 14:24:09.302423','12 chicken wings for $7.25','2022-12-27 16:04:15.95267',10000,'Chicken Wings','admin','admin',4.75,39.58,7.25,4.75,39.58,7.25,false,true,'15:00','10:30',NULL,NULL,3),
	 (131,'2022-11-23 16:16:11.77382','$10 off family style meals with code NAFTOUCHDOWN','2022-12-27 16:04:39.199445',36,NULL,'admin','admin',10.0,18.87,48.25,10.0,17.17,43.0,false,true,NULL,NULL,NULL,NULL,1),
	 (130,'2022-11-23 16:15:39.144318','$10 off family style meals with code NAFTOUCHDOWN','2022-12-27 16:04:47.983234',37,NULL,'admin','admin',10.0,18.87,48.25,10.0,17.17,43.0,false,true,NULL,NULL,NULL,NULL,1),
	 (52,'2022-07-27 23:41:12.97514','$13.60 cheese burger, fries and a 20oz fountain drink','2022-12-27 16:04:55.499962',10004,'Burger',NULL,'admin',1.65,12.13,13.6,1.65,12.13,13.6,false,true,'15:00','10:30',NULL,NULL,3),
	 (67,'2022-08-10 17:23:55.716542','$1.00 off taco salad','2022-12-27 16:05:05.147969',2,'Taco',NULL,'admin',1.0,NULL,NULL,1.0,NULL,NULL,false,true,'17:00','10:30',NULL,NULL,3),
	 (108,'2022-09-14 14:22:38.712614','Free breadsticks with purchase of a slice.','2022-12-27 16:05:14.44785',10000,'Bread','admin','admin',1.6,30.19,4.45,1.6,26.45,3.7,false,true,'15:00','10:30',NULL,NULL,3),
	 (63,'2022-07-28 00:03:39.268623','$3.00 (tax included) for any flavor small malt or root beer float.','2022-12-27 16:05:23.602026',10011,'Beverage',NULL,'admin',NULL,NULL,3.0,NULL,NULL,3.0,true,false,'15:00','10:00',NULL,NULL,3),
	 (56,'2022-07-27 23:46:38.15725','$13.45 Minnesota burger, fries and a 20oz fountain drink','2022-12-27 16:05:40.915846',10004,'Burger',NULL,'admin',1.85,12.09,13.45,1.85,12.09,13.45,false,true,'15:00','10:30',NULL,NULL,3);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified,end_time,start_time,end_date,start_date,deal_type) VALUES
	 (68,'2022-08-10 17:24:21.696795','$1.39 softshell taco','2022-12-27 16:05:56.099928',2,'Taco',NULL,'admin',NULL,NULL,1.39,NULL,NULL,1.39,false,true,'17:00','10:30',NULL,NULL,3),
	 (64,'2022-07-28 00:14:24.973642','$5.00 for two slices from 10:30 - 11:00.','2022-12-27 16:06:10.177328',10000,'Pizza',NULL,'admin',3.9,43.82,5.0,2.4,32.43,5.0,false,false,'11:00','10:30',NULL,NULL,3),
	 (59,'2022-07-27 23:59:01.748955','Two Ball Park hot dogs for $4.50','2022-12-27 16:06:20.163752',10011,'Hot Dog',NULL,'admin',0.4,8.16,4.5,0.4,8.16,4.5,false,true,'15:00','10:00',NULL,NULL,3),
	 (60,'2022-07-27 23:59:20.578794','Two Ball Park chili dogs for $5.50','2022-12-27 16:06:28.269144',10011,'Hot Dog',NULL,'admin',0.4,6.78,5.5,0.4,6.78,5.5,false,true,'15:00','10:00',NULL,NULL,3),
	 (57,'2022-07-27 23:56:35.586779','Buy one hot dog get another half off','2022-12-27 16:06:39.91432',10011,'Hot Dog',NULL,'admin',1.83,25.0,5.48,1.22,25.0,3.68,false,true,'15:00','10:00',NULL,NULL,3),
	 (66,'2022-08-10 17:11:39.566796','$1.00 off Meat and Potato Burrito ($5.21 tax included)','2022-10-12 13:27:23.934239',2,'Burrito',NULL,'admin',1.0,NULL,5.21,1.0,NULL,5.21,true,true,'15:00','10:30',NULL,NULL,3),
	 (54,'2022-07-27 23:45:21.760957','$13.75 bacon cheese burger, fries and a 20oz fountain drink','2022-10-12 13:28:25.24146',10004,'Burger',NULL,'admin',1.55,10.13,13.75,1.55,10.13,13.75,false,true,'15:00','10:30',NULL,NULL,3),
	 (124,'2022-10-13 08:07:12.995751','20% off Buddha''s Satay Bowl','2022-11-20 16:13:31.509475',38,'Bowl','admin','admin',2.09,20.0,8.4,2.09,20.0,8.4,false,true,'15:00','10:00','2022-10-31',NULL,0),
	 (125,'2022-10-13 08:09:52.726612','20% off Chipotle Cheddar Bowl','2022-11-20 16:13:45.415061',38,'Bowl','admin','admin',1.79,20.0,7.2,1.79,20.0,7.2,false,true,'15:00','10:00','2022-10-31',NULL,0),
	 (126,'2022-10-13 08:11:55.357016','20% off Teriyaki Twist Bowl','2022-11-20 16:14:00.263107',38,'Bowl','admin','admin',2.09,20.0,8.4,2.09,20.0,8.4,false,true,'15:00','10:00','2022-10-31',NULL,0);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified,end_time,start_time,end_date,start_date,deal_type) VALUES
	 (128,'2022-10-13 08:16:09.692391','20% off Oaxaca Bowl','2022-11-20 16:14:08.696675',38,'Bowl','admin','admin',2.09,20.0,8.4,2.09,20.0,8.4,false,true,'15:00','10:00','2022-10-31',NULL,0),
	 (127,'2022-10-13 08:13:30.180022','20% off Southwest BBQ Bowl','2022-11-20 16:14:26.096488',38,'Bowl','admin','admin',1.79,20.0,7.2,1.79,20.0,7.2,false,true,'15:00','10:00','2022-10-31',NULL,0),
	 (153,'2022-12-20 11:06:00.603922','Free dessert with purchase','2022-12-20 11:06:00.603922',2,NULL,'admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-12-18','2022-12-18',3),
	 (154,'2022-12-20 11:06:52.837546','Free small potato olés with purchase','2022-12-20 11:06:52.837546',2,NULL,'admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-12-19','2022-12-19',3),
	 (122,'2022-10-11 09:07:15.51212','Get a free fountain drink when you buy 2 slices, or 1 slice and a salad','2022-12-27 16:00:35.925045',30,'Beverage','admin','admin',2.25,20.81,10.72,2.25,17.35,8.56,false,false,'17:00','10:00',NULL,NULL,3),
	 (118,'2022-10-09 14:14:43.54359','From 3:00 pm to 6:00 pm:
$4 House Draft Beers,
$1 Off All Other Draft Beers,
$5 Single Premium Rail Pours,
$5 Glasses of House Wine,
$2 Off All Other Wine','2022-12-27 16:01:50.030398',17,'Beverage','admin','admin',NULL,NULL,NULL,1.0,NULL,4.0,false,false,'18:00','15:00',NULL,NULL,3),
	 (151,'2022-12-20 08:38:41.105144','%15 off purchase of $20 or more with promo code GET15 for the next 30 days. Email promotion since "We haven''t seen you in a while".','2022-12-27 16:02:14.641244',49,'Burgers','admin','admin',NULL,15.0,NULL,NULL,15.0,NULL,false,false,NULL,NULL,'2023-01-08','2022-12-11',1),
	 (150,'2022-12-20 08:36:39.13685','Lentil soup for $1.50 with entree purchase. Use code BUNDLEUP.','2022-12-27 16:02:31.295337',37,'Soup','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2023-02-28',NULL,1),
	 (159,'2022-12-27 13:50:36.64841','Lentil soup for $1.50 with entree purchase. Use code BUNDLEUP.','2022-12-27 16:02:40.639192',36,'Soup','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2023-02-28',NULL,1),
	 (152,'2022-12-20 11:05:05.403972','Try three items get a free entree. Purchase one (1) breakfast entrée, one (1) lunch or dinner entrée, and one (1) individual bakery item (“Eligible Purchases”) to receive one (1) free entrée on a future purchase.','2022-12-27 16:02:56.087536',48,NULL,'admin','admin',19.0,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2023-01-01','2022-12-18',1);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified,end_time,start_time,end_date,start_date,deal_type) VALUES
	 (158,'2022-12-27 08:49:43.124921','$1 off any breakfast combo','2022-12-27 16:03:06.351815',2,NULL,'admin','admin',1.0,NULL,NULL,1.0,NULL,NULL,false,true,NULL,NULL,'2022-12-31','2022-12-26',3),
	 (157,'2022-12-20 11:08:06.174841','$2 off any combo','2022-12-20 11:08:06.174841',2,NULL,'admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-12-23','2022-12-23',3),
	 (137,'2022-12-12 10:51:26.419431','$5 off your order of $20 or more (pre-tax) 11/09/2022 through 11/15/2022','2022-12-12 10:51:26.419431',39,NULL,'admin','admin',5.0,NULL,NULL,5.0,25.0,15.0,false,true,NULL,NULL,'2022-11-15','2022-11-09',1),
	 (138,'2022-12-12 10:56:42.913198','2 entrees for just $16 10/26/2022 through 11/01/2022','2022-12-12 10:56:42.913198',39,NULL,'admin','admin',6.58,29.14,16.0,2.58,13.89,16.0,false,true,NULL,NULL,'2022-11-01','2022-10-26',1),
	 (139,'2022-12-12 14:27:38.077877','25% off your order of $15 or more (pre-tax) 11/02/2022 through 11/08/2022','2022-12-12 14:27:38.077877',39,NULL,'admin','admin',NULL,25.0,NULL,3.75,25.0,11.25,false,true,NULL,NULL,'2022-11-08','2022-11-02',1),
	 (140,'2022-12-12 14:33:42.589079','Buy one chicken entree, get one for $5 from 11/16/2022 through 11/22/2022','2022-12-12 14:33:42.589079',39,NULL,'admin','admin',5.0,23.09,14.29,5.0,23.09,14.29,false,true,NULL,NULL,'2022-11-22','2022-11-16',1),
	 (144,'2022-12-12 14:42:11.05461','Two entrees for $16 11/30 - 12/06','2022-12-12 14:42:11.05461',39,NULL,'admin','admin',NULL,NULL,16.0,NULL,NULL,16.0,false,true,NULL,NULL,'2022-12-06','2022-11-30',1),
	 (134,'2022-12-12 09:41:13.246311','$1 off online and app orders Mondays from 11/28/22 - 12/19/22. Valid once per customer.','2022-12-12 09:41:13.246311',41,'Sandwich','admin','admin',1.0,18.9,21.99,1.0,4.55,5.29,false,true,NULL,NULL,'2022-12-19','2022-11-29',0),
	 (135,'2022-12-12 09:42:10.704865','$1 off online and app orders Mondays from 11/28/22 - 12/19/22. Valid once per customer.','2022-12-12 09:42:10.704865',42,'Sandwich','admin','admin',1.0,18.9,21.99,1.0,4.55,5.29,false,true,NULL,NULL,'2022-12-19','2022-11-29',0),
	 (136,'2022-12-12 09:43:15.592294','$1 off online and app orders Mondays from 11/28/22 - 12/19/22. Valid once per customer.','2022-12-12 09:43:15.592294',44,'Sandwich','admin','admin',1.0,18.9,21.99,1.0,4.55,5.29,false,true,NULL,NULL,'2022-12-19','2022-11-29',0);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified,end_time,start_time,end_date,start_date,deal_type) VALUES
	 (133,'2022-12-12 09:29:59.775736','$1 off online and app orders Mondays from 11/28/22 - 12/19/22. Valid once per customer.','2022-12-12 09:43:20.118898',43,'Sandwich','admin','admin',1.0,18.9,21.99,1.0,4.55,5.29,false,true,NULL,NULL,'2022-12-19','2022-11-28',0),
	 (155,'2022-12-20 11:07:16.349545','100 bonus points day','2022-12-20 11:07:16.349545',2,NULL,'admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-12-21','2022-12-21',0),
	 (132,'2022-12-12 09:26:28.956936','Buy one get one free 11/07/2022 through 11/11/2022','2022-12-12 09:26:28.956936',49,'Burgers','admin','admin',16.95,50.0,16.95,9.25,50.0,9.25,false,true,NULL,NULL,'2022-11-11','2022-11-07',1),
	 (141,'2022-12-12 14:35:23.684376','Buy one get one free sandwich on National Sandwich Day (11/03/2022)','2022-12-12 14:35:23.684376',34,'Sandwich','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-11-03','2022-11-03',1),
	 (142,'2022-12-12 14:35:44.853206','Buy one get one free sandwich on National Sandwich Day (11/03/2022)','2022-12-12 14:35:44.853206',33,'Sandwich','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-11-03','2022-11-03',1),
	 (143,'2022-12-12 14:39:15.180192','Buy one get one half off 12/05/2022 - 12/08/2022','2022-12-12 14:39:15.180192',49,'Burgers','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-12-08','2022-12-05',1),
	 (145,'2022-12-12 14:45:30.369605','Visit 1 time between 10/18/2022 and 11/1/2022 to score a FREE Original Sandwich.','2022-12-12 14:45:30.369605',34,'Sandwich','admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-11-01','2022-10-18',1),
	 (146,'2022-12-12 14:46:19.587075','Visit 1 time between 10/18/2022 and 11/1/2022 to score a FREE Original Sandwich.','2022-12-12 14:46:19.587075',33,'Sandwich','admin','admin',NULL,50.0,NULL,NULL,50.0,NULL,false,true,NULL,NULL,'2022-11-01','2022-10-18',1),
	 (149,'2022-12-14 12:33:55.902552','BOGO Free Meat and Potato Breakfast Burritos. Excludes Steak option. Via app.','2022-12-14 12:33:55.902552',2,NULL,'admin','admin',NULL,50.0,NULL,NULL,50.0,NULL,false,false,NULL,NULL,'2022-12-20','2022-12-13',0),
	 (156,'2022-12-20 11:07:46.969461','$5 off $20 mobile order','2022-12-20 11:07:46.969461',2,NULL,'admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,false,true,NULL,NULL,'2022-12-22','2022-12-22',0);
INSERT INTO public.deals (id,date_created,description,last_updated,place_id,dish,created_by,modified_by,max_discount,max_discount_percent,max_price,min_discount,min_discount_percent,min_price,tax_included,verified,end_time,start_time,end_date,start_date,deal_type) VALUES
	 (65,'2022-07-27 23:16:57.658109','$1.00 off Taco Bravo','2022-10-12 13:31:06.29389',2,'Taco',NULL,'admin',1.0,NULL,NULL,1.0,NULL,NULL,false,true,'17:00','10:30',NULL,NULL,3),
	 (55,'2022-07-27 23:46:01.33711','$13.45 mushroom swiss burger, fries and a 20oz fountain drink','2022-10-12 13:31:26.65612',10004,'Burger',NULL,'admin',1.85,12.09,13.45,1.85,12.09,13.45,false,true,'15:00','10:30',NULL,NULL,3),
	 (62,'2022-07-28 00:01:23.103103','$4.55 for a Ball Park hot dog with chips and a 24 ounce fountain drink.
$2.25 to add a dog.','2022-10-12 13:30:07.442662',10011,'Hot Dog',NULL,'admin',0.85,12.5,6.8,0.65,11.11,4.55,false,true,'15:00','10:00',NULL,NULL,3),
	 (110,'2022-09-14 14:30:16.881066','2 for 1 garlic cheese bread','2022-10-12 13:30:36.503778',10000,'Bread','admin','admin',2.4,50.0,2.4,2.4,50.0,2.4,false,true,'15:00','10:30',NULL,NULL,3);


INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (58,NULL,'2022-07-27 13:15:04.809267',1,'2022-07-27 13:15:04.809267',50,NULL,NULL),
	 (60,NULL,'2022-07-27 13:15:04.809267',0,'2022-07-27 13:15:04.809267',52,NULL,NULL),
	 (61,NULL,'2022-07-27 13:15:04.809267',1,'2022-07-27 13:15:04.809267',53,NULL,NULL),
	 (62,NULL,'2022-07-27 13:15:04.809267',2,'2022-07-27 13:15:04.809267',54,NULL,NULL),
	 (63,NULL,'2022-08-24 17:40:01.187116',1,'2022-08-24 17:40:01.187116',58,NULL,NULL),
	 (64,NULL,'2022-08-24 17:40:01.187116',4,'2022-08-24 17:40:01.187116',56,NULL,NULL),
	 (65,NULL,'2022-08-24 17:40:01.187116',4,'2022-08-24 17:40:01.187116',59,NULL,NULL),
	 (66,NULL,'2022-08-24 17:40:01.187116',0,'2022-08-24 17:40:01.187116',59,NULL,NULL),
	 (67,NULL,'2022-08-24 17:40:01.187116',3,'2022-08-24 17:40:01.187116',55,NULL,NULL),
	 (68,NULL,'2022-08-24 17:40:01.187116',3,'2022-08-24 17:40:01.187116',57,NULL,NULL);
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (69,NULL,'2022-08-24 17:46:40.112184',4,'2022-08-24 17:46:40.112184',60,NULL,NULL),
	 (70,NULL,'2022-08-24 17:40:01.187116',0,'2022-08-24 17:40:01.187116',60,NULL,NULL),
	 (71,NULL,'2022-08-24 17:40:01.187116',4,'2022-08-24 17:40:01.187116',63,NULL,NULL),
	 (72,NULL,'2022-08-24 17:40:01.187116',4,'2022-08-24 17:40:01.187116',64,NULL,NULL),
	 (73,NULL,'2022-08-24 17:40:01.187116',2,'2022-08-24 17:40:01.187116',62,NULL,NULL),
	 (75,NULL,'2022-08-24 17:40:01.187116',1,'2022-08-24 17:40:01.187116',61,NULL,NULL),
	 (76,NULL,'2022-08-24 17:40:01.187116',0,'2022-08-24 17:40:01.187116',67,NULL,NULL),
	 (77,NULL,'2022-08-24 17:40:01.187116',4,'2022-08-24 17:40:01.187116',68,NULL,NULL),
	 (78,NULL,'2022-08-24 17:59:13.720928',3,'2022-08-24 17:59:13.720928',65,NULL,NULL),
	 (127,NULL,'2022-09-06 20:39:32.032764',2,'2022-09-06 20:39:32.032707',66,NULL,NULL);
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (128,NULL,'2022-09-14 14:22:38.712201',0,'2022-09-14 14:22:38.712189',108,NULL,NULL),
	 (129,NULL,'2022-09-14 14:24:09.302029',2,'2022-09-14 14:24:09.302019',109,NULL,NULL),
	 (130,NULL,'2022-09-14 14:30:16.846915',3,'2022-09-14 14:30:16.846892',110,NULL,NULL),
	 (141,NULL,'2022-10-09 14:07:22.255393',0,'2022-10-09 14:07:22.255346',115,NULL,NULL),
	 (142,NULL,'2022-10-09 14:07:22.255472',1,'2022-10-09 14:07:22.255467',115,NULL,NULL),
	 (143,NULL,'2022-10-09 14:07:22.255502',2,'2022-10-09 14:07:22.255498',115,NULL,NULL),
	 (144,NULL,'2022-10-09 14:07:22.255527',3,'2022-10-09 14:07:22.255523',115,NULL,NULL),
	 (145,NULL,'2022-10-09 14:07:22.255549',4,'2022-10-09 14:07:22.255546',115,NULL,NULL),
	 (146,NULL,'2022-10-09 14:10:01.763604',0,'2022-10-09 14:10:01.76359',116,NULL,NULL),
	 (147,NULL,'2022-10-09 14:10:01.763637',1,'2022-10-09 14:10:01.763634',116,NULL,NULL);
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (148,NULL,'2022-10-09 14:10:01.763657',2,'2022-10-09 14:10:01.763654',116,NULL,NULL),
	 (149,NULL,'2022-10-09 14:10:01.763675',3,'2022-10-09 14:10:01.763672',116,NULL,NULL),
	 (150,NULL,'2022-10-09 14:10:01.763691',4,'2022-10-09 14:10:01.763689',116,NULL,NULL),
	 (151,NULL,'2022-10-09 14:12:25.805797',0,'2022-10-09 14:12:25.805787',117,NULL,NULL),
	 (152,NULL,'2022-10-09 14:12:25.805831',1,'2022-10-09 14:12:25.805827',117,NULL,NULL),
	 (153,NULL,'2022-10-09 14:12:25.80585',2,'2022-10-09 14:12:25.805848',117,NULL,NULL),
	 (154,NULL,'2022-10-09 14:12:25.805867',3,'2022-10-09 14:12:25.805864',117,NULL,NULL),
	 (155,NULL,'2022-10-09 14:12:25.805883',4,'2022-10-09 14:12:25.805881',117,NULL,NULL),
	 (156,NULL,'2022-10-09 14:14:43.543208',0,'2022-10-09 14:14:43.543198',118,NULL,NULL),
	 (157,NULL,'2022-10-09 14:14:43.543233',1,'2022-10-09 14:14:43.543231',118,NULL,NULL);
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (158,NULL,'2022-10-09 14:14:43.543253',2,'2022-10-09 14:14:43.543251',118,NULL,NULL),
	 (159,NULL,'2022-10-09 14:14:43.543268',3,'2022-10-09 14:14:43.543266',118,NULL,NULL),
	 (160,NULL,'2022-10-09 14:14:43.543282',4,'2022-10-09 14:14:43.54328',118,NULL,NULL),
	 (161,NULL,'2022-10-09 14:44:00.972178',0,'2022-10-09 14:44:00.972162',119,NULL,NULL),
	 (162,NULL,'2022-10-09 14:44:00.97231',1,'2022-10-09 14:44:00.972304',119,NULL,NULL),
	 (163,NULL,'2022-10-09 14:44:00.972392',2,'2022-10-09 14:44:00.972388',119,NULL,NULL),
	 (164,NULL,'2022-10-09 14:44:00.972419',3,'2022-10-09 14:44:00.972416',119,NULL,NULL),
	 (165,NULL,'2022-10-09 14:44:00.972441',4,'2022-10-09 14:44:00.972438',119,NULL,NULL),
	 (166,NULL,'2022-10-09 14:45:18.878076',0,'2022-10-09 14:45:18.878059',120,NULL,NULL),
	 (167,NULL,'2022-10-09 14:45:18.878137',1,'2022-10-09 14:45:18.87813',120,NULL,NULL);
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (168,NULL,'2022-10-09 14:45:18.878164',2,'2022-10-09 14:45:18.878161',120,NULL,NULL),
	 (169,NULL,'2022-10-09 14:45:18.878184',3,'2022-10-09 14:45:18.878181',120,NULL,NULL),
	 (170,NULL,'2022-10-09 14:45:18.878203',4,'2022-10-09 14:45:18.8782',120,NULL,NULL),
	 (171,NULL,'2022-10-11 09:05:49.963214',0,'2022-10-11 09:05:49.963191',121,NULL,NULL),
	 (172,NULL,'2022-10-11 09:05:49.963302',1,'2022-10-11 09:05:49.963295',121,NULL,NULL),
	 (173,NULL,'2022-10-11 09:05:49.963323',2,'2022-10-11 09:05:49.963321',121,NULL,NULL),
	 (174,NULL,'2022-10-11 09:05:49.963339',3,'2022-10-11 09:05:49.963337',121,NULL,NULL),
	 (175,NULL,'2022-10-11 09:05:49.963356',4,'2022-10-11 09:05:49.963354',121,NULL,NULL),
	 (176,NULL,'2022-10-11 09:07:15.511625',0,'2022-10-11 09:07:15.511609',122,NULL,NULL),
	 (177,NULL,'2022-10-11 09:07:15.511652',1,'2022-10-11 09:07:15.51165',122,NULL,NULL);
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (178,NULL,'2022-10-11 09:07:15.511669',2,'2022-10-11 09:07:15.511668',122,NULL,NULL),
	 (179,NULL,'2022-10-11 09:07:15.511685',3,'2022-10-11 09:07:15.511683',122,NULL,NULL),
	 (180,NULL,'2022-10-11 09:07:15.5117',4,'2022-10-11 09:07:15.511699',122,NULL,NULL),
	 (181,NULL,'2022-10-11 09:07:57.09405',0,'2022-10-11 09:07:57.094042',123,NULL,NULL),
	 (182,NULL,'2022-10-11 09:07:57.094072',1,'2022-10-11 09:07:57.09407',123,NULL,NULL),
	 (183,NULL,'2022-10-11 09:07:57.094088',2,'2022-10-11 09:07:57.094086',123,NULL,NULL),
	 (184,NULL,'2022-10-11 09:07:57.094103',3,'2022-10-11 09:07:57.094101',123,NULL,NULL),
	 (185,NULL,'2022-10-11 09:07:57.094117',4,'2022-10-11 09:07:57.094115',123,NULL,NULL),
	 (186,NULL,'2022-10-13 08:07:12.938042',0,'2022-10-13 08:07:12.937987',124,NULL,NULL),
	 (187,NULL,'2022-10-13 08:09:52.724649',1,'2022-10-13 08:09:52.724638',125,NULL,NULL);
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (188,NULL,'2022-10-13 08:11:55.35505',2,'2022-10-13 08:11:55.355036',126,NULL,NULL),
	 (189,NULL,'2022-10-13 08:13:30.179701',3,'2022-10-13 08:13:30.179692',127,NULL,NULL),
	 (190,NULL,'2022-10-13 08:16:09.691627',4,'2022-10-13 08:16:09.691606',128,NULL,NULL),
	 (191,NULL,'2022-11-20 16:17:39.109248',0,'2022-11-20 16:17:39.109248',129,'admin','admin'),
	 (192,NULL,'2022-11-20 16:17:39.114211',4,'2022-11-20 16:17:39.114211',129,'admin','admin'),
	 (193,NULL,'2022-12-12 09:26:28.988267',0,'2022-12-12 09:26:28.988267',132,'admin','admin'),
	 (194,NULL,'2022-12-12 09:26:28.994006',1,'2022-12-12 09:26:28.994006',132,'admin','admin'),
	 (195,NULL,'2022-12-12 09:26:28.995137',2,'2022-12-12 09:26:28.995137',132,'admin','admin'),
	 (196,NULL,'2022-12-12 09:26:28.996013',3,'2022-12-12 09:26:28.996013',132,'admin','admin'),
	 (197,NULL,'2022-12-12 09:26:28.997346',4,'2022-12-12 09:26:28.997346',132,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (198,NULL,'2022-12-12 09:29:59.779575',0,'2022-12-12 09:29:59.779575',133,'admin','admin'),
	 (205,NULL,'2022-12-12 09:41:13.250402',0,'2022-12-12 09:41:13.250402',134,'admin','admin'),
	 (212,NULL,'2022-12-12 09:42:10.713771',0,'2022-12-12 09:42:10.713771',135,'admin','admin'),
	 (219,NULL,'2022-12-12 09:43:15.594686',0,'2022-12-12 09:43:15.594686',136,'admin','admin'),
	 (226,NULL,'2022-12-12 10:51:26.450032',0,'2022-12-12 10:51:26.450032',137,'admin','admin'),
	 (227,NULL,'2022-12-12 10:51:26.454296',1,'2022-12-12 10:51:26.454296',137,'admin','admin'),
	 (228,NULL,'2022-12-12 10:51:26.45583',2,'2022-12-12 10:51:26.45583',137,'admin','admin'),
	 (229,NULL,'2022-12-12 10:51:26.459868',3,'2022-12-12 10:51:26.459868',137,'admin','admin'),
	 (230,NULL,'2022-12-12 10:51:26.461133',4,'2022-12-12 10:51:26.461133',137,'admin','admin'),
	 (231,NULL,'2022-12-12 10:51:26.462686',5,'2022-12-12 10:51:26.462686',137,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (232,NULL,'2022-12-12 10:51:26.463836',6,'2022-12-12 10:51:26.463836',137,'admin','admin'),
	 (233,NULL,'2022-12-12 10:56:42.920627',0,'2022-12-12 10:56:42.920627',138,'admin','admin'),
	 (234,NULL,'2022-12-12 10:56:42.924619',1,'2022-12-12 10:56:42.924619',138,'admin','admin'),
	 (235,NULL,'2022-12-12 10:56:42.926506',2,'2022-12-12 10:56:42.926506',138,'admin','admin'),
	 (236,NULL,'2022-12-12 10:56:42.927709',3,'2022-12-12 10:56:42.927709',138,'admin','admin'),
	 (237,NULL,'2022-12-12 10:56:42.928604',4,'2022-12-12 10:56:42.928604',138,'admin','admin'),
	 (238,NULL,'2022-12-12 10:56:42.929373',5,'2022-12-12 10:56:42.929373',138,'admin','admin'),
	 (239,NULL,'2022-12-12 10:56:42.930377',6,'2022-12-12 10:56:42.930377',138,'admin','admin'),
	 (240,NULL,'2022-12-12 14:27:38.164336',0,'2022-12-12 14:27:38.164336',139,'admin','admin'),
	 (241,NULL,'2022-12-12 14:27:38.16868',1,'2022-12-12 14:27:38.16868',139,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (242,NULL,'2022-12-12 14:27:38.169621',2,'2022-12-12 14:27:38.169621',139,'admin','admin'),
	 (243,NULL,'2022-12-12 14:27:38.170264',3,'2022-12-12 14:27:38.170264',139,'admin','admin'),
	 (244,NULL,'2022-12-12 14:27:38.171833',4,'2022-12-12 14:27:38.171833',139,'admin','admin'),
	 (245,NULL,'2022-12-12 14:27:38.174829',5,'2022-12-12 14:27:38.174829',139,'admin','admin'),
	 (246,NULL,'2022-12-12 14:27:38.175607',6,'2022-12-12 14:27:38.175607',139,'admin','admin'),
	 (247,NULL,'2022-12-12 14:33:42.593349',0,'2022-12-12 14:33:42.593349',140,'admin','admin'),
	 (248,NULL,'2022-12-12 14:33:42.59528',1,'2022-12-12 14:33:42.59528',140,'admin','admin'),
	 (249,NULL,'2022-12-12 14:33:42.596749',2,'2022-12-12 14:33:42.596749',140,'admin','admin'),
	 (250,NULL,'2022-12-12 14:33:42.598355',3,'2022-12-12 14:33:42.598355',140,'admin','admin'),
	 (251,NULL,'2022-12-12 14:33:42.600009',4,'2022-12-12 14:33:42.600009',140,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (252,NULL,'2022-12-12 14:33:42.601892',5,'2022-12-12 14:33:42.601892',140,'admin','admin'),
	 (253,NULL,'2022-12-12 14:33:42.604032',6,'2022-12-12 14:33:42.604032',140,'admin','admin'),
	 (254,NULL,'2022-12-12 14:35:23.687677',3,'2022-12-12 14:35:23.687677',141,'admin','admin'),
	 (255,NULL,'2022-12-12 14:35:44.854864',3,'2022-12-12 14:35:44.854864',142,'admin','admin'),
	 (256,NULL,'2022-12-12 14:39:15.184009',0,'2022-12-12 14:39:15.184009',143,'admin','admin'),
	 (257,NULL,'2022-12-12 14:39:15.186422',1,'2022-12-12 14:39:15.186422',143,'admin','admin'),
	 (258,NULL,'2022-12-12 14:39:15.187582',2,'2022-12-12 14:39:15.187582',143,'admin','admin'),
	 (259,NULL,'2022-12-12 14:39:15.188831',3,'2022-12-12 14:39:15.188831',143,'admin','admin'),
	 (260,NULL,'2022-12-12 14:42:11.056276',0,'2022-12-12 14:42:11.056276',144,'admin','admin'),
	 (261,NULL,'2022-12-12 14:42:11.057507',1,'2022-12-12 14:42:11.057507',144,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (262,NULL,'2022-12-12 14:42:11.058716',2,'2022-12-12 14:42:11.058716',144,'admin','admin'),
	 (263,NULL,'2022-12-12 14:42:11.059605',3,'2022-12-12 14:42:11.059605',144,'admin','admin'),
	 (264,NULL,'2022-12-12 14:42:11.060485',4,'2022-12-12 14:42:11.060485',144,'admin','admin'),
	 (265,NULL,'2022-12-12 14:42:11.061277',5,'2022-12-12 14:42:11.061277',144,'admin','admin'),
	 (266,NULL,'2022-12-12 14:42:11.062015',6,'2022-12-12 14:42:11.062015',144,'admin','admin'),
	 (267,NULL,'2022-12-12 14:45:30.374556',0,'2022-12-12 14:45:30.374556',145,'admin','admin'),
	 (268,NULL,'2022-12-12 14:45:30.377046',1,'2022-12-12 14:45:30.377046',145,'admin','admin'),
	 (269,NULL,'2022-12-12 14:45:30.379825',2,'2022-12-12 14:45:30.379825',145,'admin','admin'),
	 (270,NULL,'2022-12-12 14:45:30.381001',3,'2022-12-12 14:45:30.381001',145,'admin','admin'),
	 (271,NULL,'2022-12-12 14:45:30.383118',4,'2022-12-12 14:45:30.383118',145,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (272,NULL,'2022-12-12 14:46:19.588733',0,'2022-12-12 14:46:19.588733',146,'admin','admin'),
	 (273,NULL,'2022-12-12 14:46:19.590985',1,'2022-12-12 14:46:19.590985',146,'admin','admin'),
	 (274,NULL,'2022-12-12 14:46:19.59257',2,'2022-12-12 14:46:19.59257',146,'admin','admin'),
	 (275,NULL,'2022-12-12 14:46:19.594181',3,'2022-12-12 14:46:19.594181',146,'admin','admin'),
	 (276,NULL,'2022-12-12 14:46:19.595316',4,'2022-12-12 14:46:19.595316',146,'admin','admin'),
	 (285,NULL,'2022-12-14 12:33:55.946676',0,'2022-12-14 12:33:55.946676',149,'admin','admin'),
	 (286,NULL,'2022-12-14 12:33:55.949769',1,'2022-12-14 12:33:55.949769',149,'admin','admin'),
	 (287,NULL,'2022-12-14 12:33:55.951578',2,'2022-12-14 12:33:55.951578',149,'admin','admin'),
	 (288,NULL,'2022-12-14 12:33:55.95271',3,'2022-12-14 12:33:55.95271',149,'admin','admin'),
	 (289,NULL,'2022-12-14 12:33:55.954419',4,'2022-12-14 12:33:55.954419',149,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (290,NULL,'2022-12-20 08:36:39.177413',0,'2022-12-20 08:36:39.177413',150,'admin','admin'),
	 (291,NULL,'2022-12-20 08:36:39.181461',1,'2022-12-20 08:36:39.181461',150,'admin','admin'),
	 (292,NULL,'2022-12-20 08:36:39.182718',2,'2022-12-20 08:36:39.182718',150,'admin','admin'),
	 (293,NULL,'2022-12-20 08:36:39.183749',3,'2022-12-20 08:36:39.183749',150,'admin','admin'),
	 (294,NULL,'2022-12-20 08:36:39.184852',4,'2022-12-20 08:36:39.184852',150,'admin','admin'),
	 (295,NULL,'2022-12-20 08:38:41.10654',0,'2022-12-20 08:38:41.10654',151,'admin','admin'),
	 (296,NULL,'2022-12-20 08:38:41.107416',1,'2022-12-20 08:38:41.107416',151,'admin','admin'),
	 (297,NULL,'2022-12-20 08:38:41.108201',2,'2022-12-20 08:38:41.108201',151,'admin','admin'),
	 (298,NULL,'2022-12-20 08:38:41.108907',3,'2022-12-20 08:38:41.108907',151,'admin','admin'),
	 (299,NULL,'2022-12-20 08:38:41.109624',4,'2022-12-20 08:38:41.109624',151,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (300,NULL,'2022-12-20 11:05:05.40843',0,'2022-12-20 11:05:05.40843',152,'admin','admin'),
	 (301,NULL,'2022-12-20 11:05:05.410456',1,'2022-12-20 11:05:05.410456',152,'admin','admin'),
	 (302,NULL,'2022-12-20 11:05:05.41165',2,'2022-12-20 11:05:05.41165',152,'admin','admin'),
	 (303,NULL,'2022-12-20 11:05:05.412532',3,'2022-12-20 11:05:05.412532',152,'admin','admin'),
	 (304,NULL,'2022-12-20 11:05:05.413319',4,'2022-12-20 11:05:05.413319',152,'admin','admin'),
	 (305,NULL,'2022-12-20 11:06:00.605384',0,'2022-12-20 11:06:00.605384',153,'admin','admin'),
	 (306,NULL,'2022-12-20 11:06:00.606035',1,'2022-12-20 11:06:00.606035',153,'admin','admin'),
	 (307,NULL,'2022-12-20 11:06:00.60664',2,'2022-12-20 11:06:00.60664',153,'admin','admin'),
	 (308,NULL,'2022-12-20 11:06:00.607229',3,'2022-12-20 11:06:00.607229',153,'admin','admin'),
	 (309,NULL,'2022-12-20 11:06:00.607791',4,'2022-12-20 11:06:00.607791',153,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (310,NULL,'2022-12-20 11:06:52.839279',0,'2022-12-20 11:06:52.839279',154,'admin','admin'),
	 (311,NULL,'2022-12-20 11:07:16.350752',2,'2022-12-20 11:07:16.350752',155,'admin','admin'),
	 (312,NULL,'2022-12-20 11:07:46.970529',3,'2022-12-20 11:07:46.970529',156,'admin','admin'),
	 (313,NULL,'2022-12-20 11:08:06.176016',4,'2022-12-20 11:08:06.176016',157,'admin','admin'),
	 (314,NULL,'2022-12-27 08:49:43.172176',0,'2022-12-27 08:49:43.172176',158,'admin','admin'),
	 (315,NULL,'2022-12-27 08:49:43.175986',1,'2022-12-27 08:49:43.175986',158,'admin','admin'),
	 (316,NULL,'2022-12-27 08:49:43.177231',2,'2022-12-27 08:49:43.177231',158,'admin','admin'),
	 (317,NULL,'2022-12-27 08:49:43.178147',3,'2022-12-27 08:49:43.178147',158,'admin','admin'),
	 (318,NULL,'2022-12-27 08:49:43.195499',4,'2022-12-27 08:49:43.195499',158,'admin','admin'),
	 (319,NULL,'2022-12-27 13:50:36.835111',0,'2022-12-27 13:50:36.835111',159,'admin','admin');
INSERT INTO public.days (id,"date",date_created,day_of_week,last_updated,deal_id,created_by,modified_by) VALUES
	 (320,NULL,'2022-12-27 13:50:36.862731',1,'2022-12-27 13:50:36.862731',159,'admin','admin'),
	 (321,NULL,'2022-12-27 13:50:36.868433',2,'2022-12-27 13:50:36.868433',159,'admin','admin'),
	 (322,NULL,'2022-12-27 13:50:36.873153',3,'2022-12-27 13:50:36.873153',159,'admin','admin'),
	 (323,NULL,'2022-12-27 13:50:36.881264',4,'2022-12-27 13:50:36.881264',159,'admin','admin');

INSERT INTO public.rewards (id,created_by,date_created,last_updated,modified_by,notes,reward_type,place_id) VALUES
	 (5,'admin','2022-10-09 13:36:57.830443','2022-10-09 13:36:57.830443','admin','1 point for every $2 you spend and receive a $5 discount for every 100 points you redeem',0,16),
	 (6,'admin','2022-10-09 13:47:16.691221','2022-10-09 13:47:31.3456','admin','Earn 1 point per dollar spent. 100 points equals a $5 discount.
Exclusive app deals.
Free 1-topping solo pizza during your birthday month.',2,15),
	 (7,'admin','2022-10-09 13:48:24.654773','2022-10-09 13:48:24.654773','admin','20% off your first order.
Earn 1 point per dollar spent. 100 points equals a $5 discount.
Exclusive app deals.
Free 1-topping solo pizza during your birthday month.',2,17),
	 (8,'admin','2022-10-09 13:48:55.803239','2022-10-09 13:48:55.803239','admin','Free slice with purchase of 9 slices at full price',1,10000),
	 (9,'admin','2022-10-09 13:52:12.042715','2022-10-09 13:52:12.042715','admin','10 points per dollar spent, free items at 200, 300, 400, 500 and 2000 points.
Exclusive app deals.',2,2),
	 (10,'admin','2022-10-09 14:27:13.990587','2022-10-09 14:27:13.990587','admin',NULL,2,22),
	 (11,'admin','2022-10-09 14:29:03.086126','2022-10-09 14:29:03.086126','admin',NULL,2,23),
	 (12,'admin','2022-10-09 14:30:09.376038','2022-10-09 14:30:09.376038','admin',NULL,2,24),
	 (13,'admin','2022-10-09 14:31:00.444155','2022-10-09 14:31:00.444155','admin',NULL,2,25),
	 (14,'admin','2022-10-09 14:31:45.374188','2022-10-09 14:31:45.374188','admin',NULL,2,26);
INSERT INTO public.rewards (id,created_by,date_created,last_updated,modified_by,notes,reward_type,place_id) VALUES
	 (15,'admin','2022-10-09 14:32:48.970023','2022-10-09 14:32:48.970023','admin',NULL,2,27),
	 (16,'admin','2022-10-12 23:00:59.691905','2022-10-12 23:00:59.691905','admin','10 points for every $1 spent, 1000 points for a free entrée. Periodic perks and 2x and 3x points days.',2,33),
	 (17,'admin','2022-10-12 23:02:05.533291','2022-10-12 23:02:05.533291','admin','10 points for every $1 spent, 1000 points for a free entrée. Periodic perks and 2x and 3x points days.',2,34),
	 (18,'admin','2022-10-12 23:07:30.686721','2022-10-12 23:07:30.686721','admin','Earn 1 point for every $1 spent, $5 off for 50 points. Redeem & Earn rewards at any Cara Irish Pubs locations and on online orders. Free Birthday Reward.',0,28),
	 (19,'admin','2022-10-12 23:59:11.262565','2022-10-12 23:59:11.262565','admin',NULL,3,35),
	 (20,'admin','2022-10-13 07:41:07.741452','2022-10-13 07:41:07.741452','admin','For every $1 you spend, you''ll earn 1 point. When you reach 100 points, you''ll unlock $10 in rewards. Periodic specials.',2,36),
	 (21,'admin','2022-10-13 07:42:38.433841','2022-10-13 07:42:38.433841','admin','For every $1 you spend, you''ll earn 1 point. When you reach 100 points, you''ll unlock $10 in rewards. Periodic specials.',2,37),
	 (22,'admin','2022-10-13 08:03:17.752216','2022-10-13 08:03:17.752216','admin','1 point per dollar spent, rewards start at 50 points. Periodic offers.',2,38),
	 (24,'admin','2022-10-13 08:43:39.469235','2022-10-13 08:43:39.469235','admin','10 points for every $1 spent. Periodic offers.
https://chipotle.com/rewards',2,40),
	 (23,'admin','2022-10-13 08:36:19.335295','2022-10-13 08:45:45.106319','admin','Each dollar spent earns one "Fortune". Earn 100 Fortunes and receive a FREE Entree. Exclusive offers and promotions. https://www.leeannchin.com/rewards',2,39);
INSERT INTO public.rewards (id,created_by,date_created,last_updated,modified_by,notes,reward_type,place_id) VALUES
	 (25,'admin','2022-10-13 10:52:26.026787','2022-10-13 10:52:26.026787','admin','Free sandwich after first order. Surprise rewards
https://www.jimmyjohns.com/contact-us/faqs/',2,41),
	 (26,'admin','2022-10-13 10:53:38.590772','2022-10-13 10:53:38.590772','admin','Free sandwich after first order. Surprise rewards
https://www.jimmyjohns.com/contact-us/faqs/',2,42),
	 (27,'admin','2022-10-13 10:55:04.964589','2022-10-13 10:55:22.964462','admin','Free sandwich after first order. Surprise rewards
https://www.jimmyjohns.com/contact-us/faqs/',2,43),
	 (28,'admin','2022-10-13 10:56:29.502368','2022-10-13 10:56:29.502368','admin','Free sandwich after first order. Surprise rewards
https://www.jimmyjohns.com/contact-us/faqs/',2,44),
	 (29,'admin','2022-10-13 11:00:39.782964','2022-10-13 11:00:39.782964','admin','10 points for every $1 spent. Periodic offers.
https://chipotle.com/rewards',2,45),
	 (30,'admin','2022-10-13 11:01:14.104486','2022-10-13 11:01:14.104486','admin','10 points for every $1 spent. Periodic offers.
https://chipotle.com/rewards',2,46),
	 (31,'admin','2022-10-13 11:18:58.64721','2022-10-13 11:18:58.64721','admin','Earn 1 point per $1 spent. Periodic offers.
https://www.qdoba.com/rewards',2,47),
	 (32,'admin','2022-10-13 11:33:56.760696','2022-10-13 11:33:56.760696','admin','Earn points for purchases. Periodic offers.
https://www.panerabread.com/en-us/mypanera/rewards.html',2,48),
	 (33,'admin','2022-11-20 16:08:33.692624','2022-11-20 16:08:33.692624','admin','Periodic deals in via email. $2.00 off next order after first online order',3,49);

INSERT INTO public.deal_logs (id,created_by,date_created,last_updated,modified_by,deal_type,description,redeemed,redemption_date,deal_id,place_id) VALUES
	 (2,'admin','2022-11-02 06:10:42.777236','2022-11-20 13:34:25.408687','admin',3,'2 tacos',true,'2022-05-10',58,2),
	 (3,'admin','2022-11-20 13:41:48.384583','2022-11-20 13:41:48.384583','admin',3,'2 tacos',true,'2022-05-24',58,2),
	 (4,'admin','2022-11-20 13:43:49.779029','2022-11-20 13:43:49.779029','admin',3,'4 tacos',true,'2022-06-21',58,2),
	 (5,'admin','2022-11-20 13:46:48.759379','2022-11-20 13:46:48.759379','admin',0,'Free sandwich after purchase of sandwich at full price',true,'2022-07-29',NULL,33),
	 (8,'admin','2022-11-20 14:07:23.245416','2022-11-20 14:07:42.515592','admin',3,'3 tacos',true,'2022-09-20',58,2),
	 (9,'admin','2022-11-20 14:09:01.91368','2022-11-20 14:09:01.91368','admin',3,'2 supreme slices',true,'2022-10-04',50,10000),
	 (7,'admin','2022-11-20 13:51:33.399775','2022-11-20 14:09:12.703164','admin',3,'2 supreme slices',true,'2022-09-13',50,10000),
	 (11,'admin','2022-11-20 14:14:45.787208','2022-11-20 14:14:45.787208','admin',3,'3 tacos',true,'2022-11-08',58,2),
	 (18,'admin','2022-11-20 15:57:22.40164','2022-11-20 15:57:22.40164','admin',1,'Free sandwich after purchase of sandwich at full price',false,NULL,NULL,34),
	 (19,'admin','2022-11-20 15:59:42.061205','2022-11-20 15:59:42.061205','admin',0,'Place your first order and we will send you an offer for $5 OFF your next order of $10 or more valid for 14 days',false,NULL,NULL,39);
INSERT INTO public.deal_logs (id,created_by,date_created,last_updated,modified_by,deal_type,description,redeemed,redemption_date,deal_id,place_id) VALUES
	 (24,'admin','2022-11-20 16:09:28.477042','2022-11-20 16:09:28.477042','admin',1,'Buy one get one free 11/07/2022 through 11/11/2022',true,'2022-11-10',NULL,49),
	 (26,'admin','2022-11-21 08:53:00.986313','2022-11-21 08:53:00.986313','admin',1,'20% coupon for joining rewards good for 30 days',false,NULL,NULL,17),
	 (28,'admin','2022-11-23 17:06:01.092207','2022-11-23 17:06:01.092207','admin',0,'Buy one get one free entree',false,NULL,NULL,37),
	 (29,'admin','2022-11-23 17:06:13.601796','2022-11-23 17:06:13.601796','admin',0,'Buy one get one free entree',false,NULL,NULL,36),
	 (13,'admin','2022-11-20 14:21:46.392318','2022-11-23 17:06:46.245053','admin',1,'Buy one get one free pastrami sandwich',false,NULL,NULL,33),
	 (10,'admin','2022-11-20 14:13:29.047814','2022-11-23 17:11:59.810209','admin',4,'Free gyro with rewards points',true,'2022-09-07',NULL,16),
	 (12,'admin','2022-11-20 14:21:32.333495','2022-11-23 17:12:09.614996','admin',1,'Buy one get one free pastrami sandwich',false,NULL,NULL,34),
	 (30,'admin','2022-11-24 09:20:08.47913','2022-11-24 09:20:08.47913','admin',1,'Free sandwich after first order in app or online',false,NULL,NULL,43),
	 (31,'admin','2022-11-24 09:20:34.477522','2022-11-24 09:20:34.477522','admin',0,'Free sandwich after first order in app or online',false,NULL,NULL,41),
	 (32,'admin','2022-11-24 09:20:52.425186','2022-11-24 09:20:52.425186','admin',0,'Free sandwich after first order in app or online',false,NULL,NULL,42);
INSERT INTO public.deal_logs (id,created_by,date_created,last_updated,modified_by,deal_type,description,redeemed,redemption_date,deal_id,place_id) VALUES
	 (33,'admin','2022-11-24 09:21:09.599951','2022-11-24 09:21:09.599951','admin',0,'Free sandwich after first order in app or online',false,NULL,NULL,44),
	 (34,'admin','2022-11-30 13:11:24.449183','2022-11-30 13:11:24.449183','admin',1,'3x points 11/28/2022 only',false,NULL,NULL,47),
	 (6,'admin','2022-11-20 13:49:37.229094','2022-12-12 14:37:45.202335','admin',3,'$1 off meat and potato burrito',true,'2022-08-10',66,2);

