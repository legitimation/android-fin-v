module.exports=function(app, Comment)
{
    app.post("/comment/all", (req, res) => {
        const query = { title: req.body.title };
        console.log("title : "+JSON.stringify(query));
        Comment.find(query, function(err, comments){
            if(err) return res.status(500).send({error: 'database failure'});
            console.log("want to see all comment");
            console.log(comments);
            res.status(200).json(comments)
        })
    });

    app.post("/comment/post", (req,res) =>{
        var newComment = new Comment();
        newComment.name = req.body.name;
        newComment.title = req.body.title;
        newComment.comment = req.body.comment;
        
        console.log(JSON.stringify(newComment));
        const query = { name:newComment.name, title: newComment.title }

        Comment.findOne(query, (err, result) => {
            if (result == null) {
                newComment.save(function(err){
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

    app.delete("/comment/delete", (req,res) =>{
        const query = { comment: req.body.comment }

        Comment.findOne(query, (err, result) => {
            if (result == null) {
                res.status(400).send()
            } else {
                Comment.remove(query, function(err){
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

