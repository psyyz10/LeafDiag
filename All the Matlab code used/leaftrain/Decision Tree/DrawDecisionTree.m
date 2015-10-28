function h = DrawDecisionTree(tree,titletext)
%DRAWTREE    Draws the GPLAB trees graphically.
%     DRAWTREE(TREE,TITLE) draws the TREE in a window with TITLE. positive
%  
%     Input arguments:
%        TREE - a GPLAB tree (struct)
%        TITLE - title of the tree figure (optional,string)
%     Output arguments:
%        none
%
%   Created (2003) by SINTEF (hso@sintef.no,jtt@sintef.no,okl@sintef.no)
%   Modified (2004) by Sara Silva (sara@dei.uc.pt)
%   - removed object-oriented constructs and some "over-specific" code
%   This file is part of the GPLAB toolbox.



% Set tree titles
if nargin < 2
    titletext = 'Decision Tree found';
end
% Using new figure to display tree
h=figure; % new figure for this particular tree
% set(h,'name',titletext);
title(titletext);



% First, count nodes
[tree, count] = walkTreeDepthFirst(tree, 'countLeaves', [],  0, 0);
state.nodeCount = count;
state.yDist = -1;
% Position leaves (equally spaced)
[tree, state] = walkTreeDepthFirst(tree, 'positionLeaves', [], 0, state);
% Position parents (midway between kids)
[tree, state] = walkTreeDepthFirst(tree, [], 'positionParents', 0, state);
% Draw tree
[tree, state] = walkTreeDepthFirst(tree, [], 'drawNode', 0, 0 );

axis off



%% Subfunctions. See comments above for what they do

function [tree, state] = walkTreeDepthFirst(tree, preDive, postDive, initialDepth, state )
% Calls preDive(tree, depth, state), enters subnodes, calls postDive(tree,
% depth, state). Useful for walking the tree. 
if ~isempty(preDive)
    [tree, state] = feval(preDive, tree, initialDepth, state);
end
for i = 1:length(tree.kids)
    [tree.kids{i}, state] = walkTreeDepthFirst(tree.kids{i}, preDive, postDive, initialDepth + 1, state);
end
if ~isempty(postDive)
    [tree, state] = feval(postDive, tree, initialDepth, state);
end


function [tree, count] = countLeaves(tree, depth, count)
if isempty(tree.kids)
    tree.index = count;
    count = count + 1;
end


function [tree, state] = positionLeaves(tree, depth, state)
if isempty(tree.kids)
    if state.nodeCount <= 1
        tree.X = 0;
    else
        tree.X = tree.index / (state.nodeCount - 1);
    end
    tree.Y = depth * state.yDist;
end


function [tree, state] = positionParents(tree, depth, state)
if ~isempty(tree.kids)
    x = [];
    for i = 1:length(tree.kids)
        kid = tree.kids{i};
        x = [x kid.X];
    end
    tree.X = mean(x);    
    tree.Y = depth * state.yDist;
end


function [tree, state] = drawNode(tree, depth, state)
if ~isempty(tree.kids)
    for i = 1:length(tree.kids)
        kid = tree.kids{i};
        line([tree.X kid.X], [tree.Y kid.Y]);
    end
    line(tree.X, tree.Y, 'marker', '^', 'markersize', 8)
    opText = tree.op;
%     text(tree.X, tree.Y, ['  ' opText], 'HorizontalAlignment', 'left', 'interpreter', 'none')
    text(tree.X, tree.Y,num2str(opText),'HorizontalAlignment', 'right', 'VerticalAlignment', 'bottom','interpreter', 'none');
else
    opText = tree.op;
    line(tree.X, tree.Y, 'marker', '.', 'markersize', 8)
%     text(tree.X, tree.Y, opText, 'HorizontalAlignment', 'center', 'VerticalAlignment', 'top', 'interpreter', 'none')
    text(tree.X, tree.Y, num2str(tree.class) ,'HorizontalAlignment', 'right', 'VerticalAlignment', 'top', 'interpreter', 'none');
end
