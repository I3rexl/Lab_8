const Orders= require('../models/order');
const order = require('./order');

router.post('/add-order', async(req, res) =>{
    try {
        const data= req.body;
        const newOrder= new Orders({
            order_code: data.order_code,
            id_user: data.id_user
        })
        const result= await newOrder.save();
        if(result){
            res.json({
                "status": 200,
                "messenger": "Thêm thành công",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "messenger": "Thêm không thành công",
                "data": null
            })
        }
    } catch (error) {
        console.log(error);
        
    }
})