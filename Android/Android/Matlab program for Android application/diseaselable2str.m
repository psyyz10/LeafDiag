function [label_str] = diseaselable2str( label )

switch label
    case 1
        label_str = 'disease free';
    case 2
        label_str = 'Powdery Mildew';
    case 3
        label_str = 'Septoria';
    case 4
        label_str = 'rust';
    otherwise
        error('Invalid input');
end

end