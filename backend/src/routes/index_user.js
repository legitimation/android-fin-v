module.exports=function(app, User)
{
    app.post("/signup", (req,res) =>{

        var newUser = new User();
        newUser.name = req.body.name;
        newUser.email = req.body.email;
        newUser.password = req.body.password;

        const query = { email: newUser.email }

        User.findOne(query, (err, result) => {

            if (result == null) {
                newUser.save(function(err){
                    if (err){
                        console.error(err);
                        return;
                    }
                    res.status(200).send()
                })
            } else {
                res.status(400).send()
            }

        })

    });


    app.post("/login", (req,res) =>{
        const query = {
            email: req.body.email, 
            password: req.body.password
        }

        User.findOne(query, (err, result) => {

            if (result != null) {

                const objToSend = {
                    name: result.name,
                    email: result.email
                }

                res.status(200).send(JSON.stringify(objToSend))

            } else {
                res.status(404).send()
            }

        })
    });

    app.delete("/user/delete", (req,res) =>{
        const query = { name: req.body.name }

        User.findOne(query, (err, result) => {
            if (result == null) {
                res.status(400).send()
            } else {
                User.deleteOne(query, function(err){
                    if (err){
                        console.error(err);
                        return;
                    }
                    res.status(200).send()
                })
            }
        })
    });



}
