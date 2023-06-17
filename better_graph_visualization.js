//Width and height
var w = d3.select("body").node().getBoundingClientRect().width;
var h = window.innerHeight;

String.prototype.hashCode = function() {
    var hash = 0,
      i, chr;
    if (this.length === 0) return hash;
    for (i = 0; i < this.length; i++) {
      chr = this.charCodeAt(i);
      hash = ((hash << 5) - hash) + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  }

var colors = d3.scaleOrdinal(d3.schemeCategory10);
//Create SVG element
var svg = d3.select("body")
            .append("svg")
            .attr("width", w)
            .attr("height", h);

 //Define what to do when zooming
var offset = [0,0];
var scale = 0.25;


var graph_simulation = d3.json("https://www.lucasgover.com/MovieRecommender/movie_graph.json", function(dataset){
        //Initialize a simple force layout, using the nodes and edges in dataset
    var force = d3.forceSimulation(dataset.nodes)
                    .force("charge", d3.forceManyBody().strength(-60))
                    .force("link", d3.forceLink(dataset.edges).distance(2))
                    .force("center", d3.forceCenter().x(0).y(0));

    var update = function(){};
    var zooming = function(d) {

        //Log out d3.event.transform, so you can see all the goodies inside
        //console.log(d3.event.transform);
    
        //New offset array
        offset = [d3.event.transform.x, d3.event.transform.y];
    
        //Calculate new scale
        scale = d3.event.transform.k;

        update();
    }
    
    //Then define the zoom behavior
    var zoom = d3.zoom().on("zoom", zooming);
    
    //Create a container in which all zoom-able elements will live
    var graph = svg.append("g")
                .attr("id", "graph")
                .call(zoom)  //Bind the zoom behavior
                .call(zoom.transform, d3.zoomIdentity  //Then apply the initial transform
                    .translate(0,0) //w/2, h/2)
                    .scale(0.4));

    //Create a new, invisible background rect to catch zoom events
    graph.append("rect")
    .attr("x", 0)
    .attr("y", 0)
    .attr("width", w)
    .attr("height", h)
    .attr("opacity", 1);

    svg.append("text")
        .text("Movies Graph")
        .attr("x","50%")
        .attr("y","15%")
        .attr("text-decoration","underline");
    //Create edges as lines
    var edges = svg.selectAll("line")
        .data(dataset.edges)
        .enter()
        .append("line")
        .style("stroke", "rgba(255,255,255,0.4)")
        .style("stroke-width", 1);

    //Create nodes as circles
    var nodes = svg.selectAll("circle")
        .data(dataset.nodes)
        .enter()
        .append("circle")
        .attr("r", 10)
        .style("fill", function(d) {
            return colors(d.genres[0].hashCode() % 10);
        })
        .call(d3.drag()  //Define what to do on drag events
            .on("start", dragStarted)
            .on("drag", dragging)
            .on("end", dragEnded));

    svg.append("rect")
        .attr("x",0.5 * w - 95)
        .attr("y",0.95 * h - 26)
        .attr("width",190)
        .attr("height",36)
        .attr("fill","white")
        .attr("opacity",0.4);

    svg.append("a")
        .attr("href","https://github.com/lwgover/MovieRecommender")
        .append("text")
        .text("[Source Code]")
        .attr("x","50%")
        .attr("y","95%")
        .style("font-size","24pt")
        .style("pointer-events","all")
        .attr("text-decoration","underline");

    
    update = function() { 
        edges.attr("x1", function(d) { return scale * (d.source.x + (offset[0])) + w/2; })
            .attr("y1", function(d) { return scale * (d.source.y + offset[1]) + h/2; })
            .attr("x2", function(d) { return scale * (d.target.x + offset[0]) + w/2; })
            .attr("y2", function(d) { return scale * ( d.target.y + offset[1])+h/2; })
            .style("stroke-width", scale);

        nodes.attr("cx", function(d) { return scale * (d.x + offset[0]) + w/2; })
            .attr("cy", function(d) { return scale * (d.y + offset[1])  + h/2; })
            .attr("r", 10* scale);
    }

    //show title div
    nodes.on("mouseover", function(d) {

        //Get this bar's x/y values, then augment for the tooltip
        var xPosition = parseFloat(d3.select(this).attr("cx"));
        var yPosition = parseFloat(d3.select(this).attr("cy")) + 20;

        //Update the tooltip position and value
        d3.select("#tooltip")
            .style("left", xPosition + "px")
            .style("top", yPosition + "px")						
            .select("#name")
            .text(d.name);

        //Update the tooltip position and value
                d3.select("#tooltip")
                    .style("left", xPosition + "px")
                    .style("top", yPosition + "px")
                    .select("#genres")
                    .text(d.genres.toString());

        //Show the tooltip
        d3.select("#tooltip").classed("hidden", false);

    })
    .on("mouseout", function() {

        //Hide the tooltip
        d3.select("#tooltip").classed("hidden", true);
        
    })

    //Add a simple tooltip
    nodes.append("title")
            .text(function(d) {
            return d.name;
            });

    var update = function() { 
        edges.attr("x1", function(d) { return scale * (d.source.x + (offset[0])) + w/2; })
            .attr("y1", function(d) { return scale * (d.source.y + offset[1]) + h/2; })
            .attr("x2", function(d) { return scale * (d.target.x + offset[0]) + w/2; })
            .attr("y2", function(d) { return scale * ( d.target.y + offset[1])+h/2; })
            .style("stroke-width", scale);

        nodes.attr("cx", function(d) { return scale * (d.x + offset[0]) + w/2; })
            .attr("cy", function(d) { return scale * (d.y + offset[1])  + h/2; })
            .attr("r", 10* scale);
    }

    //Every time the simulation "ticks", this will be called
    force.on("tick", function() {
        update();
    });

    //Define drag event functions
    function dragStarted(d) {
        if (!d3.event.active) force.alphaTarget(0.3).restart();
        d.fx = d.x;
        d.fy = d.y;
    }

    function dragging(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }

    function dragEnded(d) {
        if (!d3.event.active) force.alphaTarget(0);
        d.fx = null;
        d.fy = null;
    }
});
