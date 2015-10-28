function [label] = str2diseaselable( label_str )

switch label_str
    case 'disease free'
        label = 1;
    case 'Powdery Mildew'
        label = 2;
    case 'Septoria'
        label = 3;
    case 'rust'
        label = 4;
    otherwise
        error('Invalid input');
end

end