/*
Copyright © 2019, Florian Dormont

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

The Software is provided “as is”, without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders X be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the Software.
Except as contained in this notice, the name of the <copyright holders> shall not be used in advertising or otherwise to promote the sale, use or other dealings in this Software without prior written authorization from the <copyright holders>.
*/

///////// WalrusRPG's battle background shader
/// Requires:
/// - A pattern texture
/// - A colormap/palette vertical strip texture
/// - uniform variables to be passed to set the shader
/// - At least a polygon to render with on screen. (Can be set to show the shader on a part of the screen)
/////////

////// Global settings

// 'time' contains seconds since the program was linked.
uniform float time;
// tex is a sampler to the pattern to use
uniform sampler2D tex;
// palette is a sample to a 1*(up to 256) pixels picture
uniform sampler2D palette;
// Size of the palette.
uniform int NB_colors;

// Pattern scale (in pixels).
uniform vec2 v_scale;

// Speed of the global translation.
uniform vec2 v_global_offset;


////// Color cycling variables

// Global color cycling speed
// TODO : move to two variables for each cycle
uniform float v_cycling_speed;
/// First cycle
// First index of the cycle's range
uniform int v_cycling_1_start;
// Last index of the cycle's range. Must be higer than _start.
uniform int v_cycling_1_end;
/// Second cycle
uniform int v_cycling_2_start;
uniform int v_cycling_2_end;

////// Distorsions
/// Scanline offset : the background is offset on the right / left per-y.
// Frequency
uniform float vf_scanline_offset;
// Speed
uniform float vs_scanline_offset;
// Amplitude
uniform float va_scanline_offset;

/// Scanline offset : the background is offset on y per-y.
uniform float vf_scanline_wave;
uniform float vs_scanline_wave;
uniform float va_scanline_wave;

// The formula behind all the distorsions. A simple A*sin(v*f + t*s)
float offset(float value, float amplitude, float frequency, float speed)
{
	return amplitude * sin(value * frequency + time * speed);
}

// The coordinates distorsion routine
vec2 distorsion()
{
    // Getting the global offset
	vec2 global_offset = vec2(time*v_global_offset.x,
							  time*v_global_offset.y);
    // Getting the distorsions value
	float scanline_offset = offset(gl_FragCoord.y, va_scanline_offset,
                                   vf_scanline_offset, vs_scanline_offset);
	float scanline_wave = offset(gl_FragCoord.y, va_scanline_wave,
                                   vf_scanline_wave, vs_scanline_wave);
    // Summing everything and returning the value
	return (gl_FragCoord.xy / vec2(v_scale.x, v_scale.y)
            + vec2(scanline_offset, scanline_wave) + global_offset);
}

// Color cycling managment. As we can't edit the textures, the shader is supposed to do the cycling work.
float cycling_bound(float c, int c_min, int c_max, float speed)
{
    // Checking if we are in the bounds.
	if (c_min != c_max && c >= float(c_min) && c <= float(c_max))
	{
		c += time*speed;
        // Clamping the modulo value to stay in the cycle range.
		c = mod(c - float(c_min), float(c_max-c_min)) + float(c_min);
	}
	return c;
}
void main()
{
    // Getting the right texture coordinates for the current fragment.
	vec2 texUV = distorsion();

    // THe red channel contains a value between [0-255]/255. which indexes the color to use.
	float texture = texture2D(tex, texUV).r;
   	float c = texture * 255.;

    // First color cycle range pass
	c = cycling_bound(c, v_cycling_1_start, v_cycling_1_end, v_cycling_speed);
    // Second color cycle range pass
	c = cycling_bound(c, v_cycling_2_start, v_cycling_2_end, v_cycling_speed);

    // Getting the resulting color	
    vec3 palcolor = texture2D(palette, vec2(0., c / float(NB_colors))).rgb;
    // And we're done! :D
	gl_FragColor = vec4(palcolor, 1.);
}