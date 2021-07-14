module.exports=function(app, Post)
{

    app.post("/post", (req,res) =>{
        var newPost = new Post();
        newPost.name = req.body.name;
        newPost.title = req.body.title;
        newPost.content = req.body.content;
        newPost.namesofliked=req.body.namesofliked;
        console.log(JSON.stringify(newPost));
        const query = { title: newPost.title }

        Post.findOne(query, (err, result) => {
            if (result == null) {
                newPost.save(function(err){
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

    app.get("/post/all", (req, res) => {
        Post.find(function(err, posts){
            if(err) return res.status(500).send({error: 'database failure'});
            console.log(posts);
            console.log(typeof(posts[0]));
            res.status(200).json(posts)
        })
    });

    app.delete("/post/delete", (req,res) =>{
        const query = { title: req.body.title }

        Post.findOne(query, (err, result) => {
            if (result == null) {
                res.status(400).send()
            } else {
                Post.remove(query, function(err){
                    if (err){
                        console.error(err);
                        return;
                    }
                    res.status(200).send()
                })
            }
        })
    });


    app.post("/post/update", (req,res) =>{
        console.log("업데이트로 들어 옴");

        Post.findOne({title:req.body.title}, function(err, post){
            if(err) return res.status(500).json({ error: 'database failure' });
            if(!post) return res.status(404).json({ error: 'book not found' });

            if(req.body.name) post.name = req.body.name;
            if(req.body.title) post.title = req.body.title;
            if(req.body.content) post.content = req.body.content;
            if(req.body.namesofliked) post.namesofliked = req.body.namesofliked;

            post.save(function(err){
                if(err) 
                {res.status(500);
                    console.log("변경 실패");
                }
                else{
                    console.log("변경 성공");
                    res.status(200).json(post);
                }
            });
        });
    });
    
}
