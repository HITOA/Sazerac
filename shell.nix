with import <nixpkgs> {
  config.allowUnfree = true;
};
pkgs.mkShell {
  nativeBuildInputs = with pkgs; [ 
    openjdk
    gradle
    wayland
    wayland-scanner
    xorg.libX11
    xorg.libXrandr
    xorg.libXinerama
    xorg.libXcursor
    xorg.libXi
    libxkbcommon
    libGL
    glfw
    libglvnd
  ];

  NIX_LD_LIBRARY_PATH = lib.makeLibraryPath [
    stdenv.cc.cc
    wayland
    wayland-scanner
    xorg.libX11
    xorg.libXrandr
    xorg.libXinerama
    xorg.libXcursor
    xorg.libXi
    libxkbcommon
    libGL
    glfw
    libglvnd
  ];

  NIX_LD = lib.fileContents "${stdenv.cc}/nix-support/dynamic-linker";
}